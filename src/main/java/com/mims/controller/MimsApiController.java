package com.mims.controller;

import com.mims.auth.MimsUserPrincipal;
import com.mims.model.Domain;
import com.mims.model.ChangePasswordRequest;
import com.mims.model.CreateUserRequest;
import com.mims.model.Invention;
import com.mims.model.InventionQuery;
import com.mims.model.Opportunity;
import com.mims.model.Request;
import com.mims.model.ResetPasswordRequest;
import com.mims.model.Review;
import com.mims.model.UpdateProfileRequest;
import com.mims.model.UpdateStatusRequest;
import com.mims.model.UpdateUserRequest;
import com.mims.model.UserAccount;
import com.mims.service.MimsAuthenticationService;
import com.mims.service.MimsDataService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class MimsApiController {

    private final MimsDataService dataService;
    private final MimsAuthenticationService authenticationService;

    public MimsApiController(MimsDataService dataService,
                             MimsAuthenticationService authenticationService) {
        this.dataService = dataService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/domains")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public List<Domain> domains() {
        return dataService.getDomains();
    }

    @GetMapping("/domains/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Domain domain(@PathVariable Long id) {
        return dataService.getDomain(id);
    }

    @PostMapping("/domains")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Domain> createDomain(@Valid @RequestBody Domain domain) {
        Domain created = dataService.createDomain(domain);
        return created("/api/domains/" + created.id(), created);
    }

    @PutMapping("/domains/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Domain updateDomain(@PathVariable Long id, @Valid @RequestBody Domain domain) {
        return dataService.updateDomain(id, domain);
    }

    @DeleteMapping("/domains/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        dataService.deleteDomain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserAccount> users(@RequestParam(required = false) String userType,
                                   @RequestParam(required = false) String status) {
        return dataService.getUsers(userType, status);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserAccount user(@PathVariable Long id) {
        return dataService.getUser(id);
    }

    @GetMapping("/profile")
    public UserAccount profile(@AuthenticationPrincipal MimsUserPrincipal user) {
        return dataService.getUser(user.getId());
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserAccount> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserAccount created = dataService.createUser(request);
        return created("/api/users/" + created.id(), created);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserAccount updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return dataService.updateUser(id, request);
    }

    @PatchMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public UserAccount updateUserStatus(@PathVariable Long id,
                                        @Valid @RequestBody UpdateStatusRequest request) {
        return dataService.updateUserStatus(id, request.status());
    }

    @PatchMapping("/users/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id,
                                               @Valid @RequestBody ResetPasswordRequest request) {
        if (!authenticationService.resetPassword(id, request.newPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " was not found");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile")
    public UserAccount updateProfile(@Valid @RequestBody UpdateProfileRequest request,
                                     @AuthenticationPrincipal MimsUserPrincipal user) {
        return dataService.updateProfile(user.getId(), request);
    }

    @PatchMapping("/profile/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                               @AuthenticationPrincipal MimsUserPrincipal user) {
        if (!authenticationService.changePassword(user.getId(),
                request.currentPassword(), request.newPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inventions")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'INVENTOR')")
    public List<Invention> inventions(@RequestParam(required = false) Long domainId,
                                      @AuthenticationPrincipal MimsUserPrincipal user) {
        boolean ownOnly = "INVENTOR".equalsIgnoreCase(user.getUserType());
        return dataService.getInventions(user.getId(), ownOnly, domainId);
    }

    @GetMapping("/inventions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'INVENTOR')")
    public Invention invention(@PathVariable Long id,
                               @AuthenticationPrincipal MimsUserPrincipal user) {
        Invention invention = dataService.getInvention(id);
        requireOwnerOrStaff(invention.userId(), user);
        return invention;
    }

    @PostMapping("/inventions")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'INVENTOR')")
    public ResponseEntity<Invention> createInvention(
            @Valid @RequestBody Invention invention,
            @AuthenticationPrincipal MimsUserPrincipal user) {
        Long ownerId = "INVENTOR".equalsIgnoreCase(user.getUserType())
                ? user.getId()
                : invention.userId();
        Invention created = dataService.createInvention(invention, ownerId);
        return created("/api/inventions/" + created.id(), created);
    }

    @PutMapping("/inventions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'INVENTOR')")
    public Invention updateInvention(
            @PathVariable Long id,
            @Valid @RequestBody Invention invention,
            @AuthenticationPrincipal MimsUserPrincipal user) {
        Invention existing = dataService.getInvention(id);
        requireOwnerOrStaff(existing.userId(), user);
        Long ownerId = "INVENTOR".equalsIgnoreCase(user.getUserType())
                ? user.getId()
                : invention.userId();
        return dataService.updateInvention(id, new Invention(id, invention.domainId(),
                invention.title(), invention.thesis(), invention.finalDocument(),
                invention.registeredDate(), invention.patentDate(), invention.status(), ownerId));
    }

    @DeleteMapping("/inventions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'INVENTOR')")
    public ResponseEntity<Void> deleteInvention(
            @PathVariable Long id,
            @AuthenticationPrincipal MimsUserPrincipal user) {
        Invention existing = dataService.getInvention(id);
        requireOwnerOrStaff(existing.userId(), user);
        dataService.deleteInvention(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public List<Request> requests(@RequestParam(required = false) String status,
                                  @RequestParam(required = false) Long reviewerId) {
        return dataService.getRequests(status, reviewerId);
    }

    @GetMapping("/requests/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Request request(@PathVariable Long id) {
        return dataService.getRequest(id);
    }

    @PostMapping("/requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public ResponseEntity<Request> createRequest(@Valid @RequestBody Request request,
                                                 @AuthenticationPrincipal MimsUserPrincipal user) {
        Request created = dataService.createRequest(request, user.getId());
        return created("/api/requests/" + created.id(), created);
    }

    @PutMapping("/requests/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Request updateRequest(@PathVariable Long id, @Valid @RequestBody Request request) {
        return dataService.updateRequest(id, request);
    }

    @DeleteMapping("/requests/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        dataService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'DOMAIN_EXPERT')")
    public List<Review> reviews(@RequestParam(required = false) Long inventionId,
                                @AuthenticationPrincipal MimsUserPrincipal user) {
        boolean ownOnly = "Domain Expert".equalsIgnoreCase(user.getUserType());
        return dataService.getReviews(user.getId(), ownOnly, inventionId);
    }

    @GetMapping("/reviews/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'DOMAIN_EXPERT')")
    public Review review(@PathVariable Long id,
                         @AuthenticationPrincipal MimsUserPrincipal user) {
        Review review = dataService.getReview(id);
        requireReviewOwnerOrStaff(review.reviewerId(), user);
        return review;
    }

    @PostMapping("/reviews")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'DOMAIN_EXPERT')")
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review,
                                               @AuthenticationPrincipal MimsUserPrincipal user) {
        Review created = dataService.createReview(review, user.getId());
        return created("/api/reviews/" + created.id(), created);
    }

    @PutMapping("/reviews/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR', 'DOMAIN_EXPERT')")
    public Review updateReview(@PathVariable Long id,
                               @Valid @RequestBody Review review,
                               @AuthenticationPrincipal MimsUserPrincipal user) {
        Review existing = dataService.getReview(id);
        requireReviewOwnerOrStaff(existing.reviewerId(), user);
        return dataService.updateReview(id, review);
    }

    @DeleteMapping("/reviews/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        dataService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/queries")
    @PreAuthorize("isAuthenticated()")
    public List<InventionQuery> queries(@RequestParam(required = false) Long inventionId,
                                        @RequestParam(required = false) Long replyId) {
        return dataService.getQueries(inventionId, replyId);
    }

    @GetMapping("/queries/{id}")
    @PreAuthorize("isAuthenticated()")
    public InventionQuery query(@PathVariable Long id) {
        return dataService.getQuery(id);
    }

    @PostMapping("/queries")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InventionQuery> createQuery(
            @Valid @RequestBody InventionQuery query,
            @AuthenticationPrincipal MimsUserPrincipal user) {
        if ("Inventor".equalsIgnoreCase(user.getUserType())) {
            Invention invention = dataService.getInvention(query.inventionId());
            requireOwnerOrStaff(invention.userId(), user);
        }
        InventionQuery created = dataService.createQuery(query);
        return created("/api/queries/" + created.id(), created);
    }

    @PutMapping("/queries/{id}")
    @PreAuthorize("isAuthenticated()")
    public InventionQuery replyToQuery(
            @PathVariable Long id,
            @Valid @RequestBody InventionQuery query,
            @AuthenticationPrincipal MimsUserPrincipal user) {
        InventionQuery existing = dataService.getQuery(id);
        return dataService.updateQuery(id, new InventionQuery(id, existing.inventionId(),
                existing.query(), user.getId(), query.reply()));
    }

    @DeleteMapping("/queries/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
        dataService.deleteQuery(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/opportunities")
    @PreAuthorize("isAuthenticated()")
    public List<Opportunity> opportunities(@RequestParam(required = false) Long domainId,
                                           @RequestParam(required = false) String status) {
        return dataService.getOpportunities(domainId, status);
    }

    @GetMapping("/opportunities/{id}")
    @PreAuthorize("isAuthenticated()")
    public Opportunity opportunity(@PathVariable Long id) {
        return dataService.getOpportunity(id);
    }

    @PostMapping("/opportunities")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public ResponseEntity<Opportunity> createOpportunity(@Valid @RequestBody Opportunity opportunity) {
        Opportunity created = dataService.createOpportunity(opportunity);
        return created("/api/opportunities/" + created.id(), created);
    }

    @PutMapping("/opportunities/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
    public Opportunity updateOpportunity(@PathVariable Long id,
                                         @Valid @RequestBody Opportunity opportunity) {
        return dataService.updateOpportunity(id, opportunity);
    }

    @DeleteMapping("/opportunities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        dataService.deleteOpportunity(id);
        return ResponseEntity.noContent().build();
    }

    private <T> ResponseEntity<T> created(String path, T entity) {
        return ResponseEntity.created(URI.create(path)).body(entity);
    }

    private void requireOwnerOrStaff(Long ownerId, MimsUserPrincipal user) {
        if ("INVENTOR".equalsIgnoreCase(user.getUserType())
                && (ownerId == null || !ownerId.equals(user.getId()))) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
    }

    private void requireReviewOwnerOrStaff(Long reviewerId, MimsUserPrincipal user) {
        if ("Domain Expert".equalsIgnoreCase(user.getUserType())
                && (reviewerId == null || !reviewerId.equals(user.getId()))) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
    }
}
