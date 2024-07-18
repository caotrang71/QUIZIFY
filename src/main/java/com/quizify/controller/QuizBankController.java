package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.*;
import com.quizify.repository.UserRepository;
import org.jsoup.Jsoup;
import com.quizify.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz-banks")
public class QuizBankController {
    @Autowired
    private QuizBankService quizBankService;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionChoiceService questionChoiceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private NotificationsRepsitory notificationsRepsitory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private UserService userService;

    //view list of quiz banks
    @GetMapping("/quiz-banks-list")
    public String quizBankList(Model model, @Param("keyword") String keyword) {
        List<QuizBank> quizBanksList = quizBankService.getAllQuizBanks();
        if (keyword != null && !keyword.isEmpty()) {
            quizBanksList = this.quizBankService.searchQuizBank(keyword);
            model.addAttribute("keyword", keyword);
        }
        model.addAttribute("quizBanksList", quizBanksList);
        return "quiz-bank-list";
    }

    @GetMapping("/quiz-bank-detail/{id}")
    public String getDetailQuizBank(@PathVariable(value="id") long id, Model model, HttpSession session){
        QuizBank quizBank = quizBankService.getQuizBankById(id);
        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
        for(Question question : questions) {
            List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
            questionChoicesMap.put(question, questionChoices);
        }
        //view star voted
        User user = (User) session.getAttribute("user");
        if (user!=null){
            int star = voteService.getStarVoteByUser(user.getId(),id);
            model.addAttribute("star",star);
        }
        //view all user voted
        List<Vote> votes = voteService.getAllUserVoted(id);
        model.addAttribute("votes",votes);
        //view average star
        Double average = voteService.getAverageStar(id);
        model.addAttribute("averageStar", average);
        //List comments
        List<Comments> commentsList = commentsService.getAllCommentByQuizBanksID(id);
        model.addAttribute("commentsList", commentsList);

        model.addAttribute("quizBank", quizBank);
        model.addAttribute("questions", questions);
        model.addAttribute("questionChoicesMap", questionChoicesMap);

        return "quiz-bank-detail";
    }

    //form to create quiz bank
    @GetMapping("/create-quiz-bank")
    public String createQuizBank(Model model) {
        model.addAttribute("quizBank", new QuizBank());
        return "create-quiz-bank";
    }

    @PostMapping("/created")
    public String createQuizBank(@ModelAttribute QuizBank quizBank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-quiz-bank";
        }

        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "create-quiz-bank";
        }

        for (Question question : quizBank.getQuestions()) {
//            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
//                model.addAttribute("error", "Each question must have exactly 4 choices.");
//                return "create-quiz-bank";
//            }
            for (QuestionChoice choice : question.getQuestionChoices()) {
                System.out.println(choice.getCorrectOrNot());
                choice.setQuestion(question);
            }
        }

        quizBankService.createQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-banks-list";
    }

    @ModelAttribute("categoryList")
    public List<Category> getCategories() {
        return categoryService.getAllCategories()
                .stream().map(item -> {
            Category category = new Category();
            BeanUtils.copyProperties(item, category);
            return category;
        }).collect(Collectors.toList());
    }

    @ModelAttribute("subcategoryList")
    public List<Subcategory> getSubcategories() {
        return subcategoryService.getAllSubcategories()
                .stream().map(item -> {
            Subcategory subcategory = new Subcategory();
            BeanUtils.copyProperties(item, subcategory);
            return subcategory;
        }).collect(Collectors.toList());
    }

    //form to update quiz bank
    @GetMapping("/update-quiz-bank/{id}")
    public String updateQuizBank(@PathVariable(value="id") long id, Model model) {
        QuizBank quizBank = quizBankService.getQuizBankById(id);
        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
        for(Question question : questions) {
            List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
            questionChoicesMap.put(question, questionChoices);
        }
        model.addAttribute("quizBank", quizBank);
        model.addAttribute("questions", questions);
        model.addAttribute("questionChoicesMap", questionChoicesMap);
        return "update-quiz-bank";
    }

    //back to list after saved updating successfully
    @PostMapping("/saved")
    public String saveQuizBank(@ModelAttribute("quizBank") QuizBank quizBank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-quiz-bank";
        }
        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "update-quiz-bank";
        }
        for (Question question : quizBank.getQuestions()) {
//            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
//                model.addAttribute("error", "Each question must have exactly 4 choices.");
//                return "update-quiz-bank";
//            }
            question.setId(null);
            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setId(null);
                choice.setQuestion(question);
            }
        }

        // Save the QuizBank, Questions, and Choices
        quizBankService.updateQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-banks-list";
    }


    @GetMapping("/delete-quiz-bank/{id}")
    public String deleteQuizBank(@PathVariable("id") Long id, Model model) {
        quizBankService.deleteQuizBankById(id);
        return "redirect:/quiz-banks/quiz-banks-list";
    }

    @PostMapping("/vote")
    public String voteQuizBanks(@RequestParam("userID") long userID,
                                @RequestParam("quizBanksID") long quizBanksID,
                                @RequestParam("star") Integer star,
                                @RequestParam long receivedBy,
                                @RequestParam String link,
                                RedirectAttributes redirectAttributes)
    {
        voteService.saveVote(userID,quizBanksID,star);
        String title = "you have new vote";
        String content = "Your quiz bank has just been "+ star + " voted star";
        User user = userRepository.findById(userID).orElse(null);
        notificationsService.saveNotification(title, content,user,receivedBy,link,false);

        return "redirect:/quiz-banks/quiz-banks-list";
    }

    //save comments
    @PostMapping("/comment")
    public String saveCommentQuizBanks(@RequestParam("userID") long userID,
                                       @RequestParam("quizBanksID") long quizBanksID,
                                       @RequestParam("content") String content,
                                       @RequestParam long receivedBy,
                                       @RequestParam String link,
                                       RedirectAttributes redirectAttributes)
    {
        // save comment
        User user = userService.findById(userID);
        commentsService.saveComment(content,user,quizBanksID);
        //save notification
        String title = "Quiz Bank have new comments";
        String contentNotification = user.getFullName() + " commented on Quiz Banks";
        notificationsService.saveNotification(title,contentNotification,user,receivedBy,link,false);
        redirectAttributes.addFlashAttribute("commentSuccess", true);
        return "redirect:/quiz-banks/quiz-bank-detail/"+quizBanksID;
    }
    //change comment
    @GetMapping("/edit/comment/{id}")
    public String showChangeComment(@PathVariable long id, Model model,RedirectAttributes redirectAttributes){
        Comments oldComment = commentsRepository.findById(id).orElse(null);
        if (oldComment != null) {
            model.addAttribute("oldComment", oldComment);
            return "change_comment";
        }else {
            redirectAttributes.addFlashAttribute("mess", "comment not found");
            return "quiz-bank-detail";
        }
    }
    @PostMapping("/edit/comment")
    public String changeComment(@RequestParam long commentID,@RequestParam String content, RedirectAttributes redirectAttributes) {
        Comments comment = commentsRepository.findById(commentID).orElse(null);
        commentsService.changeComment(content,commentID);
        return "redirect:/quiz-banks/quiz-bank-detail/" + comment.getQuizBanksID();
    }
    // delete comment
    @DeleteMapping("/delete/comment/{id}")
    @ResponseBody
    public void deleteCommnet(@PathVariable long id) {
        commentsService.deleteComment(id);
    }

    // make notifications as read
    @PutMapping("/notifications/mark-as-read/{id}")
    @ResponseBody
    public void markAsRead(@PathVariable long id) {
        Notifications notification = notificationsRepsitory.findById(id).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationsRepsitory.save(notification);
        }
    }

    // make notifications as new
    @PutMapping("/notifications/mark-as-new/{id}")
    @ResponseBody
    public void markAsNew(@PathVariable long id) {
        Notifications notification = notificationsRepsitory.findById(id).orElse(null);
        if (notification != null) {
            notification.setRead(false);
            notificationsRepsitory.save(notification);
        }
    }

    @DeleteMapping("/notifications/delete/{id}")
    @ResponseBody
    public void deleteNotification(@PathVariable long id) {
        Notifications notification = notificationsRepsitory.findById(id).orElse(null);
        if (notification != null) {
            notificationsRepsitory.delete(notification);
        }
    }


}

