package com.akashpriya.wedding.controller;

import com.akashpriya.wedding.service.MemoryWallService;
import com.akashpriya.wedding.service.impl.GoogleDriveStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Admin area: view pending Memory Wall posts, approve or delete them.
 * Protected by Spring Security (ROLE_ADMIN) - see SecurityConfig.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MemoryWallService memoryWallService;
    private final GoogleDriveStorageService googleDriveStorageService;

    public AdminController(MemoryWallService memoryWallService, GoogleDriveStorageService googleDriveStorageService) {
        this.memoryWallService = memoryWallService;
        this.googleDriveStorageService = googleDriveStorageService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pendingPosts", memoryWallService.getPendingPosts());
        model.addAttribute("approvedPosts", memoryWallService.getApprovedPosts());

        long[] quota = googleDriveStorageService.getStorageQuota();
        if (quota != null) {
            model.addAttribute("driveUsedGb", String.format("%.2f", quota[0] / (1024.0 * 1024 * 1024)));
            model.addAttribute("driveTotalGb", String.format("%.2f", quota[1] / (1024.0 * 1024 * 1024)));
            model.addAttribute("drivePercentUsed", (int) Math.round((quota[0] * 100.0) / quota[1]));
        }
        return "admin";
    }

    @PostMapping("/memory/{id}/approve")
    public String approve(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        memoryWallService.approve(id);
        redirectAttributes.addFlashAttribute("adminSuccess", "Post approved successfully.");
        return "redirect:/admin";
    }

    @PostMapping("/memory/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        memoryWallService.delete(id);
        redirectAttributes.addFlashAttribute("adminSuccess", "Post deleted successfully.");
        return "redirect:/admin";
    }
}

