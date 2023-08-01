package net.codejava.aws.restcontroller;

import net.codejava.aws.S3Util;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class ImageDownload {

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> handleImageDownload(@RequestParam("fileName") String fileName) {
        try {
// Download the image from S3 using the S3Util class
            InputStream imageStream = S3Util.downloadFile(fileName);

// Set the content type and headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type based on the image type

// Create an InputStreamResource to wrap the image stream
            InputStreamResource resource = new InputStreamResource(imageStream);

// Return the ResponseEntity with the image data in the response body
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception ex) {
// Handle any errors that occur during the download process
            String errorMessage = "Error downloading image: " + ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes())));
        }
    }
}
