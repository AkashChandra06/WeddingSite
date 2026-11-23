package com.akashpriya.wedding.controller;

import com.akashpriya.wedding.service.FileStorageService;
import com.akashpriya.wedding.service.MemoryWallService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles guest submissions to the Memory Wall, including optional file upload
 * (photo, video, or voice note) with validation. New posts require admin approval.
 */
@Controller
@RequestMapping("/memory-wall")
public class MemoryWallController {

    private final MemoryWallService memoryWallService;
    private final FileStorageService fileStorageService;

    public MemoryWallController(MemoryWallService memoryWallService, FileStorageService fileStorageService) {
        this.memoryWallService = memoryWallService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/submit")
    public String submit(@RequestParam(name = "guestName") @NotBlank String guestName,
                          @RequestParam(name = "relationship", required = false) String relationship,
                          @RequestParam(name = "message") @NotBlank String message,
                          @RequestParam(name = "file", required = false) MultipartFile file,
                          RedirectAttributes redirectAttributes) {
        try {
            String storedFileName = null;
            String fileType = null;
            String filePath = null;

            if (file != null && !file.isEmpty()) {
                storedFileName = fileStorageService.store(file);
                fileType = fileStorageService.resolveFileType(file);
                filePath = "/uploads/" + storedFileName;
            }

            memoryWallService.submit(guestName, relationship, message, storedFileName, fileType, filePath);

            redirectAttributes.addFlashAttribute("memorySuccess",
                    "Thank you! Your message has been submitted and will appear after admin approval.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("memoryError", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("memoryError",
                    "Something went wrong while submitting your memory. Please try again.");
        }
        return "redirect:/#memory-wall";
    }
}

