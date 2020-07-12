package com.example.demo.api.repositories;

import com.example.demo.api.models.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageFile, String> {

    @Query("SELECT img FROM ImageFile img WHERE img.fileName = :imageName")
    ImageFile findImageByNameFile(String imageName);

}
