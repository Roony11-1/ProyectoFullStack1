package review.service.review_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import review.service.review_service.model.Review;
import review.service.review_service.repository.ReviewRepository;

@Service
public class ReviewService 
{
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviews()
    {
        return reviewRepository.findAll();
    }

    public Review getReviewByProductoId(int productoId)
    {
        return reviewRepository.findById(productoId).orElse(null);
    }
    
    public Review registrar(Review review)
    {
        Review nuevo = reviewRepository.save(review);
        return nuevo;
    }

    public void borrar(int id)
    {
        reviewRepository.deleteById(id);
    }
}
