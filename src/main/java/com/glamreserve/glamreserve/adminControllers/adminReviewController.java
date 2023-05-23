package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.reserve.Reserve;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.review.Review;
import com.glamreserve.glamreserve.entities.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/admin/adminReviews")
    public ModelAndView adminReview() {
        ModelAndView modelAndView = new ModelAndView("admin/adminReviews");
        Review review = new Review();
        modelAndView.addObject("review", review);
        List<Review> reviews = reviewRepository.findAll();
        modelAndView.getModelMap().addAttribute("reviews", reviews);
        return modelAndView;
    }
    @PostMapping("/adminReviewAdd")
    public ModelAndView adminReviewAdd (@ModelAttribute("review") Review review, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReviews");

        reviewRepository.save(review);
            atri.addFlashAttribute("success", "Reseña guardada con éxito");

        return modelo;
    }

    @PostMapping("/adminReviewUpdate")
    public ModelAndView adminReviewUpdate (@ModelAttribute("review") Review review, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReviews");

        //Service serviceToUpdate = serviceRepository.getUserById(service.getId());
        reviewRepository.save(review);

            atri.addFlashAttribute("success", "Reseña actualizada con éxito");

            return modelo;
    }



   @GetMapping("/adminReviewUpdateForm/{id}")
    public ModelAndView updateReserve(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminReviewUpdateForm");
       Review review = reviewRepository.findById(id).get();

       modelo.addObject("review",review);

        return modelo;
    }

    @GetMapping("/deleteReview/{id}")
    public ModelAndView deleteReview(@PathVariable(name="id") Long id, RedirectAttributes atri){

        //Review review = reviewRepository.findById(id).get();
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReviews");

        reviewRepository.deleteById(id);
            atri.addFlashAttribute("success", "Reseña eliminada con éxito");


        return modelo;
    }


}
