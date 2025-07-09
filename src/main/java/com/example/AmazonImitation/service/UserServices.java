package com.example.AmazonImitation.service;

import com.example.AmazonImitation.entity.User;
import com.example.AmazonImitation.model.GitModel;
import com.example.AmazonImitation.model.UserRequestModel;
import com.example.AmazonImitation.model.UserResponseModel;
import com.example.AmazonImitation.repositery.UserRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;
import java.util.Objects;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    public UserResponseModel touserentity(UserRequestModel userRequestModel) {
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

        File localPath = new File(gitModel.getTempFolder());
        if (localPath.exists()) {
            deleteDir(localPath.toPath());
        }

        System.out.println("Cloning repository...");
        Git git = Git.cloneRepository()
                .setURI(gitModel.getGitUrl())
                .setDirectory(localPath)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitModel.getUsername(), gitModel.getToken()))
                .call();

        System.out.println("Checking out base branch: " + gitModel.getBaseBranch());
        git.checkout().setName(gitModel.getBaseBranch()).call();
        git.pull().call();

        System.out.println("Creating new branch: " + gitModel.getNewBranch());
        git.checkout().setCreateBranch(true).setName(gitModel.getNewBranch()).call();

        System.out.println("Searching and removing flags...");
        removeFlagFromFiles(Paths.get(gitModel.getTempFolder()), gitModel.getFlagToRemove(),gitModel.getFlagToReplace());

        System.out.println("Staging and committing changes...");
        git.add().addFilepattern(".").call();
        git.commit().setMessage(gitModel.getNewBranch() + ": Removed changes").call();

        System.out.println("Pushing changes...");
        git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitModel.getUsername(), gitModel.getToken()))
                .call();

        System.out.println("✅ Process completed successfully.");
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
                                System.out.println("Flag removed from: " + path);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
