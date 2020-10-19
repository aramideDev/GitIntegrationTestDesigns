/**
 * 
 */
package com.voxgen.gitintegration.commit;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import com.voxgen.gitintegration.model.CommitHistoryModel;
import com.voxgen.gitintegration.model.GitRepoModel;

/**
 * @author ashobande
 *
 */
public class GitCommitLogHandler {
	
	private GitRepoModel gitRepositoryModel;
	
	private Git gitInstance;
	
	private HashMap<Integer, CommitHistoryModel> commitHistoryList;
	
	public GitCommitLogHandler(GitRepoModel model) {
		
		this.gitRepositoryModel = model;
		this.commitHistoryList = new HashMap<>();
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
	
	/**
	 * Max # of returned commits from history (used by default)
	 * 
	 * @param maxLimit
	 * @return
	 */
	public String listRepoCommitsOnLimit(int maxLimit) {
		
		String result = null;
		Integer index = 0;
		
		try {

			Iterable<RevCommit> logs = 
					this.gitInstance.log()
					.setMaxCount(maxLimit).call();
			
			for (RevCommit rev : logs) {

				Instant revisionCommitTimeRaw = Instant.ofEpochSecond(rev.getCommitTime());
				String revisionCommitTime = revisionCommitTimeRaw.toString() + "\n";
				String revisionMessage = rev.getFullMessage() + "\n";
				String revisionId = rev.getId().getName();
				String revisionAuthor = rev.getAuthorIdent().getName();
				String revisionEmail = rev.getAuthorIdent().getEmailAddress();

				System.out.print(revisionCommitTime);
				System.out.print(": ");
				System.out.print(revisionMessage);
				System.out.println();
				System.out.println(revisionId);
				System.out.print(revisionAuthor);
				System.out.println(revisionEmail);
				System.out.println("-------------------------");

				//TODO -->> each value, capture & concat for a 4-column table (to view changes via UI)
				//			or just show line by line in a textArea???
				this.commitHistoryList.put(index, new CommitHistoryModel(revisionId, revisionAuthor, revisionEmail, revisionMessage, revisionCommitTime));
				index++;
			}
			
			result = "Commit history presented.";
			
		} catch (GitAPIException e) {
			
			e.printStackTrace();
			System.out.println("\nError: " + e.getMessage().toString());
		}
		
		System.out.println(result);
		return result;
		
	}
	
	public String listAllRepoCommits() {
		
		String result = null;
		Integer index = 0;
		
		try {

			Iterable<RevCommit> logs = this.gitInstance.log().all().call();
			
			for (RevCommit rev : logs) {

				Instant revisionCommitTimeRaw = Instant.ofEpochSecond(rev.getCommitTime());
				String revisionCommitTime = revisionCommitTimeRaw.toString() + "\n";
				String revisionMessage = rev.getFullMessage() + "\n";
				String revisionId = rev.getId().getName();
				String revisionAuthor = rev.getAuthorIdent().getName();
				String revisionEmail = rev.getAuthorIdent().getEmailAddress();

				System.out.print(revisionCommitTime);
				System.out.print(": ");
				System.out.print(revisionMessage);
				System.out.println();
				System.out.println(revisionId);
				System.out.print(revisionAuthor);
				System.out.println(revisionEmail);
				System.out.println("-------------------------");

				//TODO -->> each value, capture & concat for a 4-column table (to view changes via UI)
				//			or just show line by line in a textArea???
				this.commitHistoryList.put(index, new CommitHistoryModel(revisionId, revisionAuthor, revisionEmail, revisionMessage, revisionCommitTime));
				index++;
			}
			
			result = "Commit history presented.";
			
		} catch (GitAPIException | IOException e) {
			
			e.printStackTrace();
			System.out.println("\nError: " + e.getMessage().toString());
		}
		
		System.out.println(result);
		return result;
		
	}
	
	public HashMap<Integer, CommitHistoryModel> collectCommitHistoryResults() {
		
		return this.commitHistoryList;
	}

}
