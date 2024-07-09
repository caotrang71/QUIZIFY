package com.quizify.service;

import com.quizify.model.Comments;
import com.quizify.model.User;
import com.quizify.repository.CommentsRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public void saveComment(String comment, User user, long quizBanksID) {
        Comments newComment = new Comments();
        //dung thu vien jsoup de xoá các thẻ html có trong comment
        String commentSanitized = Jsoup.parse(comment).text();
        newComment.setComment(commentSanitized);

        newComment.setUser(user);
        newComment.setQuizBanksID(quizBanksID);
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

    public List<Comments> getAllCommentByQuizBanksID(long quizBanksID) {
        return commentsRepository.findByQuizBanksID(quizBanksID);
    }
}
