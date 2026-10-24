package com.mims.service;

import com.mims.model.Domain;
import com.mims.model.CreateUserRequest;
import com.mims.model.Invention;
import com.mims.model.InventionQuery;
import com.mims.model.Opportunity;
import com.mims.model.PublicRequest;
import com.mims.model.Request;
import com.mims.model.Review;
import com.mims.model.UpdateProfileRequest;
import com.mims.model.UpdateUserRequest;
import com.mims.model.UserAccount;
import com.mims.repository.MimsRepository;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MimsDataService {

    private final MimsRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MimsDataService(MimsRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Domain> getDomains() {
        return repository.findDomains();
    }

    public Domain getDomain(Long id) {
        return repository.findDomain(id).orElseThrow(() -> notFound("Domain", id));
    }

    @Transactional
    public Domain createDomain(Domain domain) {
        return repository.insertDomain(new Domain(repository.nextId("MIMS_DOMAINS_SEQ"),
                domain.name(), domain.description(), domain.advisorsCount(), domain.expertsCount()));
    }

    @Transactional
    public Domain updateDomain(Long id, Domain domain) {
        getDomain(id);
        Domain updated = new Domain(id, domain.name(), domain.description(),
                domain.advisorsCount(), domain.expertsCount());
        repository.updateDomain(updated);
        return updated;
    }

    @Transactional
    public void deleteDomain(Long id) {
        if (repository.deleteDomain(id) == 0) {
            throw notFound("Domain", id);
        }
    }

    public List<UserAccount> getUsers(String userType, String status) {
        return repository.findUsers(userType, status);
    }

    public UserAccount getUser(Long id) {
        return repository.findUser(id).orElseThrow(() -> notFound("User", id));
    }

    @Transactional
    public UserAccount createUser(CreateUserRequest request) {
        String userType = normalizeUserType(request.userType());
        if (!Set.of("Admin", "Advisor", "Domain Expert", "Inventor", "User").contains(userType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported user type");
        }
        validateDomainAssignment(userType, request.userTypeId());
        Long id = repository.insertUserDetails(
                request.name(), request.mobile(), request.email(), request.address());
        repository.insertLoginDetails(id, request.username(), passwordEncoder.encode(request.password()),
                userType, request.userTypeId(), request.startDate(), request.endDate());
        adjustDomainCount(userType, request.userTypeId(), 1);
        return getUser(id);
    }

    @Transactional
    public UserAccount updateUser(Long id, UpdateUserRequest request) {
        UserAccount current = getUser(id);
        validateDomainAssignment(current.userType(), request.userTypeId());
        if (repository.updateUserDetails(id, request.name(), request.mobile(),
                request.email(), request.address()) != 1
                || repository.updateUserTypeId(id, request.userTypeId()) != 1) {
            throw notFound("User", id);
        }
        if (!java.util.Objects.equals(current.userTypeId(), request.userTypeId())) {
            adjustDomainCount(current.userType(), current.userTypeId(), -1);
            adjustDomainCount(current.userType(), request.userTypeId(), 1);
        }
        return getUser(id);
    }

    @Transactional
    public UserAccount updateProfile(Long id, UpdateProfileRequest request) {
        getUser(id);
        if (repository.updateUserDetails(id, request.name(), request.mobile(),
                request.email(), request.address()) != 1
                || repository.updateUsername(id, request.username()) != 1) {
            throw notFound("User", id);
        }
        return getUser(id);
    }

    @Transactional
    public UserAccount updateUserStatus(Long id, String status) {
        UserAccount current = getUser(id);
        if (!"Active".equalsIgnoreCase(status) && !"Inactive".equalsIgnoreCase(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status must be Active or Inactive");
        }
        String normalizedStatus = status.toLowerCase(Locale.ROOT).equals("active")
                ? "Active"
                : "Inactive";
        if (normalizedStatus.equalsIgnoreCase(current.status())) {
            return current;
        }
        if (repository.updateUserStatus(id, normalizedStatus) != 1) {
            throw notFound("User", id);
        }
        adjustDomainCount(current.userType(), current.userTypeId(),
                "Active".equals(normalizedStatus) ? 1 : -1);
        return getUser(id);
    }

    public List<Invention> getInventions(Long userId, boolean restrictToUser, Long domainId) {
        return repository.findInventions(domainId, restrictToUser ? userId : null);
    }

    public Invention getInvention(Long id) {
        return repository.findInvention(id).orElseThrow(() -> notFound("Invention", id));
    }

    @Transactional
    public Invention createInvention(Invention invention, Long userId) {
        return repository.insertInvention(new Invention(repository.nextId("MIMS_INVENTIONS_SEQ"),
                invention.domainId(), invention.title(), invention.thesis(), invention.finalDocument(),
                invention.registeredDate(), invention.patentDate(), invention.status(), userId));
    }

    @Transactional
    public Invention updateInvention(Long id, Invention invention) {
        getInvention(id);
        Invention updated = new Invention(id, invention.domainId(), invention.title(), invention.thesis(),
                invention.finalDocument(), invention.registeredDate(), invention.patentDate(),
                invention.status(), invention.userId());
        repository.updateInvention(updated);
        return updated;
    }

    @Transactional
    public void deleteInvention(Long id) {
        if (repository.deleteInvention(id) == 0) {
            throw notFound("Invention", id);
        }
    }

    public List<Request> getRequests(String status, Long reviewerId) {
        return repository.findRequests(status, reviewerId);
    }

    public Request getRequest(Long id) {
        return repository.findRequest(id).orElseThrow(() -> notFound("Request", id));
    }

    @Transactional
    public Request createRequest(Request request, Long userId) {
        return repository.insertRequest(new Request(repository.nextId("MIMS_REQUESTS_SEQ"),
                request.name(), request.phoneNumber(), request.email(), request.address(),
                request.domainId(), request.reviewDate(), request.reviewerId(), request.comments(),
                request.status(), userId));
    }

    @Transactional
    public Request createPublicRequest(PublicRequest request) {
        getDomain(request.domainId());
        return repository.insertRequest(new Request(repository.nextId("MIMS_REQUESTS_SEQ"),
                request.name(), request.phoneNumber(), request.email(), request.address(),
                request.domainId(), "", null, "", "New", null));
    }

    @Transactional
    public Request updateRequest(Long id, Request request) {
        getRequest(id);
        Request updated = new Request(id, request.name(), request.phoneNumber(), request.email(),
                request.address(), request.domainId(), request.reviewDate(), request.reviewerId(),
                request.comments(), request.status(), request.userId());
        repository.updateRequest(updated);
        return updated;
    }

    @Transactional
    public void deleteRequest(Long id) {
        if (repository.deleteRequest(id) == 0) {
            throw notFound("Request", id);
        }
    }

    public List<Review> getReviews(Long reviewerId, boolean restrictToReviewer, Long inventionId) {
        return repository.findReviews(inventionId, restrictToReviewer ? reviewerId : null);
    }

    public Review getReview(Long id) {
        return repository.findReview(id).orElseThrow(() -> notFound("Review", id));
    }

    @Transactional
    public Review createReview(Review review, Long reviewerId) {
        return repository.insertReview(new Review(repository.nextId("MIMS_REVIEWS_SEQ"),
                review.inventionId(), review.reviewDate(), reviewerId, review.comments()));
    }

    @Transactional
    public Review updateReview(Long id, Review review) {
        getReview(id);
        Review updated = new Review(id, review.inventionId(), review.reviewDate(),
                review.reviewerId(), review.comments());
        repository.updateReview(updated);
        return updated;
    }

    @Transactional
    public void deleteReview(Long id) {
        if (repository.deleteReview(id) == 0) {
            throw notFound("Review", id);
        }
    }

    public List<InventionQuery> getQueries(Long inventionId, Long replyId) {
        return repository.findQueries(inventionId, replyId);
    }

    public InventionQuery getQuery(Long id) {
        return repository.findQuery(id).orElseThrow(() -> notFound("Query", id));
    }

    @Transactional
    public InventionQuery createQuery(InventionQuery query) {
        return repository.insertQuery(new InventionQuery(repository.nextId("MIMS_QUERIES_SEQ"),
                query.inventionId(), query.query(), null, null));
    }

    @Transactional
    public InventionQuery updateQuery(Long id, InventionQuery query) {
        getQuery(id);
        InventionQuery updated = new InventionQuery(id, query.inventionId(), query.query(),
                query.replyId(), query.reply());
        repository.updateQuery(updated);
        return updated;
    }

    @Transactional
    public void deleteQuery(Long id) {
        if (repository.deleteQuery(id) == 0) {
            throw notFound("Query", id);
        }
    }

    public List<Opportunity> getOpportunities(Long domainId, String status) {
        return repository.findOpportunities(domainId, status);
    }

    public Opportunity getOpportunity(Long id) {
        return repository.findOpportunity(id).orElseThrow(() -> notFound("Opportunity", id));
    }

    @Transactional
    public Opportunity createOpportunity(Opportunity opportunity) {
        return repository.insertOpportunity(new Opportunity(
                repository.nextId("MIMS_OPPORTUNITIES_SEQ"), opportunity.domainId(),
                opportunity.offer(), opportunity.description(), opportunity.status()));
    }

    @Transactional
    public Opportunity updateOpportunity(Long id, Opportunity opportunity) {
        getOpportunity(id);
        Opportunity updated = new Opportunity(id, opportunity.domainId(), opportunity.offer(),
                opportunity.description(), opportunity.status());
        repository.updateOpportunity(updated);
        return updated;
    }

    @Transactional
    public void deleteOpportunity(Long id) {
        if (repository.deleteOpportunity(id) == 0) {
            throw notFound("Opportunity", id);
        }
    }

    private ResponseStatusException notFound(String entity, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " " + id + " was not found");
    }

    private String normalizeUserType(String userType) {
        if (userType == null) {
            return "";
        }
        return switch (userType.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ")) {
            case "admin" -> "Admin";
            case "advisor" -> "Advisor";
            case "domain expert" -> "Domain Expert";
            case "inventor" -> "Inventor";
            case "user" -> "User";
            default -> userType.trim();
        };
    }

    private void validateDomainAssignment(String userType, Long domainId) {
        if (("Advisor".equals(userType) || "Domain Expert".equals(userType))
                && domainId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Advisor and Domain Expert accounts require a domain");
        }
        if (domainId != null) {
            getDomain(domainId);
        }
    }

    private void adjustDomainCount(String userType, Long domainId, int adjustment) {
        if (domainId == null) {
            return;
        }
        String countColumn = switch (userType) {
            case "Advisor" -> "advisors_count";
            case "Domain Expert" -> "experts_count";
            default -> null;
        };
        if (countColumn != null && repository.adjustDomainCount(domainId, countColumn, adjustment) != 1) {
            throw notFound("Domain", domainId);
        }
    }
}
