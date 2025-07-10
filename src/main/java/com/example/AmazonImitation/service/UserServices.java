package com.example.AmazonImitation.service;

import com.example.AmazonImitation.configuration.GitProperties;
import com.example.AmazonImitation.entity.User;
import com.example.AmazonImitation.model.GitModel;
import com.example.AmazonImitation.model.UserRequestModel;
import com.example.AmazonImitation.model.UserResponseModel;
import com.example.AmazonImitation.repositery.UserRepository;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;
import java.util.Objects;


@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GitProperties gitProperties;

    static Logger logger = LoggerFactory.getLogger(UserServices.class);


    public UserResponseModel toUserEntity(UserRequestModel userRequestModel) {
        if (userRequestModel == null) {
            return null;
        } else {
            User user = new User();
            user.setFirstname(userRequestModel.getFirstname());
            user.setLastname(userRequestModel.getLastname());
            user.setMobileno(userRequestModel.getMobileno());
            user.setEmail(userRequestModel.getEmail());
            user.setUsername(userRequestModel.getUsername());
            user.setPassword(userRequestModel.getPassword());
            return tousermodel(userRepository.save(user));
        }
    }

    public UserResponseModel tousermodel(User user) {
        if (user == null) {
            return null;
        } else {
            UserResponseModel userResponseModel = new UserResponseModel();
            userResponseModel.setFirstname(user.getFirstname());
            userResponseModel.setLastname(user.getLastname());
            userResponseModel.setMobileno(user.getMobileno());
            userResponseModel.setEmail(user.getEmail());
            userResponseModel.setUsername(user.getUsername());
            userResponseModel.setPassword(user.getPassword());
            return userResponseModel;
        }
    }


    public boolean authenticate(String username,String password)
    {
        if(userRepository.existsById(username))
        {
            User userList=  userRepository.getById(username);
            boolean result;
            if(Objects.equals(userList.getPassword(), password)) {
                result=true;
            }
            else{
                result=false;
            }
            return result;
       }
    else {
            return false;
        }
    }

    public void completeGitProcess(GitModel gitModel) throws IOException, GitAPIException {

        try {
            logger.info("Verifying access to repository...");
            LsRemoteCommand lsRemote = Git.lsRemoteRepository()
                    .setHeads(true)
                    .setRemote(gitProperties.getUrl())
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                            gitProperties.getUsername(), gitProperties.getToken()));

            lsRemote.call();
        } catch (Exception e) {
            logger.error("Unauthorized to access repo : {} ", gitProperties.getUrl());
            return;
        }

        File localPath = new File(gitModel.getTempFolder());
        if (localPath.exists()) {
            deleteDir(localPath.toPath());
        }

        logger.info("Cloning repository...");
        Git git = Git.cloneRepository()
                .setURI(gitProperties.getUrl())
                .setDirectory(localPath)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitProperties.getUsername(), gitProperties.getToken()))
                .call();

        logger.info("Checking out base branch: {} " , gitModel.getBaseBranch());
        git.checkout().setName(gitModel.getBaseBranch()).call();
        git.pull().call();

        logger.info("Creating new branch: {}" , gitModel.getNewBranch());
        git.checkout().setCreateBranch(true).setName(gitModel.getNewBranch()).call();

        logger.info("Searching and removing flags...");
        removeFlagFromFiles(Paths.get(gitModel.getTempFolder()), gitModel.getFlagToRemove(),gitModel.getFlagToReplace());

        logger.info("Staging and committing changes...");
        git.add().addFilepattern(".").call();
        git.commit().setMessage(gitModel.getNewBranch() + ": Removed changes").call();

        logger.info("Pushing changes...");
        git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitProperties.getUsername(), gitProperties.getToken()))
                .call();

        logger.info("✅ Process completed successfully.");
    }

    private static void removeFlagFromFiles(Path root, String flag,String replace) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            if (content.contains(flag)) {
                                String updated = content.replace(flag, replace);
                                Files.writeString(path, updated);
                                logger.info("Flag removed from: {} " , path);
                            }
                        } catch (IOException e) {
                            logger.error("Error in removing flag : {}",e.getMessage());
                        }
                    });
        }
    }

    private static void deleteDir(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a)) // delete children first
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
