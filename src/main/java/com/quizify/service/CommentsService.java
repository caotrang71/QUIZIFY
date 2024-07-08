package com.quizify.service;

import com.quizify.model.Comments;
import com.quizify.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public void saveComment(String comment,long userID,long quizBanksID) {
        Comments newComment = new Comments();

        newComment.setComment(comment);
        newComment.setComment_by(userID);
        newComment.setQuiz_banks_id(quizBanksID);
        commentsRepository.save(newComment);
    }

    public void updateComment(long commentID,String comment) {
        Comments oldComment = commentsRepository.findById(commentID).orElse(null);
        oldComment.setComment(comment);
        commentsRepository.save(oldComment);
    }

    public void deleteComment(long commentID) {
        Comments comments = commentsRepository.findById(commentID).orElse(null);
        if(comments != null) {
            commentsRepository.delete(comments);
        }
    }
}
