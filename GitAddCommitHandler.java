package com.voxgen.gitintegration.commit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.voxgen.gitintegration.model.GitRepoModel;

/**
 * @author ashobande
 *
 */
public class GitAddCommitHandler {
	
	private GitRepoModel gitRepositoryModel;
	
	private Git gitInstance;
	
	public GitAddCommitHandler(GitRepoModel model) {
		
		this.gitRepositoryModel = model;
	}
	
	public void connectLocalRepository() {
		
		try {
			
			this.gitInstance = Git.open(new File(this.gitRepositoryModel.getLocalFileSystemClonePath()));
			
			System.out.println("Successfully connected to local repo.");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println("\nError: " + e.getMessage().toString());
		}
		
	}
	
	public String addUntrackedFile(String fileName) {
		
		String result = null;
		Path repoPath = Paths.get(this.gitRepositoryModel.getLocalFileSystemClonePath());
		
		try {
			
			this.gitInstance.add().addFilepattern(fileName).call();
			
			result = fileName + " added to 'Staged changes'.";
			
		} catch (GitAPIException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "\nError: " + e.getMessage().toString();
			
		}
		
		System.out.println(result);
		return result;
		
	}
	
	public String addMultipleUntrackedFiles(HashMap<String, String> filesToAdd) {
		
		String result = null;
		
		try {
			
			//iteratively add all
			for (String fileNameKey : filesToAdd.keySet()) {
				
				final String fileName = filesToAdd.get(fileNameKey);
				
				this.gitInstance.add().addFilepattern(fileName).call();
				
				result = fileName + " added to 'Staged changes'.";
				System.out.println(result);
			}
			
		} catch (GitAPIException e) {
			
			e.printStackTrace();
			result = "\nError: " + e.getMessage().toString();
			
		}
		
		System.out.println(result);
		return result;
	}
	
	public String addUntrackedFileWithCommit(String fileName, String commitMsg,
												String email) {
		
		String result = null;
		Path repoPath = Paths.get(this.gitRepositoryModel.getLocalFileSystemClonePath());
		
		try {
			
			this.gitInstance.add().addFilepattern(fileName).call();
			
			this.gitInstance.commit().setMessage(commitMsg)
					.setAuthor("author", email).call();
			
			result = fileName + " added to 'Staged changes'.";
			
		} catch (GitAPIException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "\nError: " + e.getMessage().toString();
			
		}
		
		System.out.println(result);
		return result;
		
	}
	
	//TODO --> auth tracked files??
	

}
