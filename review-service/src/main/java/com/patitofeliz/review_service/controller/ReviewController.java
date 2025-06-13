package com.patitofeliz.review_service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patitofeliz.review_service.model.Review;
import com.patitofeliz.review_service.service.ReviewService;

@RestController
@RequestMapping("/review")


public class ReviewController 
{
    @Autowired
    private ReviewService reviewService;

   @GetMapping
    public ResponseEntity<List<EntityModel<Review>>> listarReviews()
    {
        List<Review> reviews = reviewService.getReviews();

        if(reviews.isEmpty())
            return ResponseEntity.noContent().build();
        
        return ResponseEntity.ok(hateoasPlural(reviews));
     }   

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Review>> obtenerReviewId(@PathVariable("id") int id)
    {
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(hateoasSingular(review));
     }

   @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Review>> obtenerReviewPorProductoId(@PathVariable("productoId") int productoId)
    {
        List<Review> reviews = reviewService.getReviewByProductoId(productoId);

        if (reviews == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable int id) 
    {   
        boolean existe = reviewService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Review>> registrarReview(@RequestBody Review review)
    {
        Review reviewNueva = reviewService.registrar(review);

        return ResponseEntity.ok(hateoasSingular(reviewNueva));
     }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> borrarReview(@PathVariable int id)
    {
        Review review = reviewService.getReview(id);

        if (review == null)
            return ResponseEntity.notFound().build();

        reviewService.borrar(id);
            
        return ResponseEntity.noContent().build();
     }

    // Metodos que me entregan los hateoas -- me chorie de ponerlos uno a uno xD como los weones ya basta
    private EntityModel<Review> hateoasSingular(Review review) 
    {
        int id = review.getId();

        return EntityModel.of(review,
            linkTo(methodOn(ReviewController.class).obtenerReviewId(id)).withSelfRel(),
            linkTo(methodOn(ReviewController.class).listarReviews()).withRel("GET/reviews"),
            linkTo(methodOn(ReviewController.class).obtenerReviewPorProductoId(review.getProductoId())).withRel("GET/reviewProducto")
        );
    }

    private List<EntityModel<Review>> hateoasPlural(List<Review> reviews) 
    {
        List<EntityModel<Review>> listaconLinks = new ArrayList<>();

        for (Review review : reviews) {
            EntityModel<Review> recurso = EntityModel.of(review,
                linkTo(methodOn(ReviewController.class).obtenerReviewId(review.getId())).withSelfRel()
            );
            listaconLinks.add(recurso);
        }

        return listaconLinks;
    }
}
