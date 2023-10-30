package it.almaviva.zeebe.manager.integration.jgitclient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Component;

import it.almaviva.zeebe.manager.integration.jgitclient.domain.LogResult;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class JGitClient {
    
    private Git git;

    public void initialize(String repoPath, boolean isNewRepo) throws Exception {
        if (isNewRepo == true)
            git = Git.init().setDirectory(new File(repoPath)).call();
        else
            git = Git.open(new File(repoPath));
    }

    public void add(String filePattern) throws Exception{
        AddCommand add = git.add();
        add.addFilepattern(filePattern);
        add.call();
    }
    
    public void checkout(String commitSha, String pathFile) throws Exception {
    	
    	CheckoutCommand checkout=git.checkout();
    	checkout.addPath(pathFile);
    	checkout.setStartPoint(commitSha);
    	checkout.call();
    	
    }

    public void commit(String author, String email, String message) throws Exception{
        CommitCommand commit = git.commit();
        commit.setAuthor(author, email);
        commit.setMessage(message);
        commit.call();
    }

    public List<LogResult> getFileLog(String pathFile) throws Exception {
        
        ArrayList<LogResult> logResults = new ArrayList<>();
        LogResult logResult;
        
        LogCommand log = git.log(); 
        if(pathFile != null) log.addPath(pathFile);
        Iterable<RevCommit> logs = log.all().call();
        
        for (RevCommit aCommit : logs){
            logResult = new LogResult();
            logResult.setEmail(aCommit.getAuthorIdent().getEmailAddress());
            logResult.setName(aCommit.getAuthorIdent().getName());
            logResult.setCommitSha(aCommit.getId().getName());
            logResult.setComment(aCommit.getFullMessage());
            logResult.setFileName(pathFile);
            logResults.add(logResult);
        }

        return logResults;
    }

	public Git getGit() {
		return git;
	}
    
    
}