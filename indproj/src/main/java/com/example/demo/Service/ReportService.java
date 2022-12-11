package com.example.demo.Service;

import com.example.demo.models.Post;
import com.example.demo.models.Profile;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ReportService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> listAll() {
        return postRepository.findAll(Sort.by("datapos").ascending());
    }
}
