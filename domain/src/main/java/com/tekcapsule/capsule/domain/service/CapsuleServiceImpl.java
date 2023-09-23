package com.tekcapsule.capsule.domain.service;

import com.tekcapsule.capsule.domain.command.*;
import com.tekcapsule.capsule.domain.model.*;
import com.tekcapsule.capsule.domain.repository.CapsuleDynamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CapsuleServiceImpl implements CapsuleService {
    private CapsuleDynamoRepository capsuleDynamoRepository;


    @Autowired
    public CapsuleServiceImpl(CapsuleDynamoRepository capsuleDynamoRepository) {
        this.capsuleDynamoRepository = capsuleDynamoRepository;
    }

    @Override
    public void create(CreateCommand createCommand) {

        log.info(String.format("Entering create capsule service - Capsule Title:%s", createCommand.getTitle()));

        Capsule capsule = Capsule.builder()
                .audience(createCommand.getAudience())
                .author(createCommand.getAuthor())
                .description(createCommand.getDescription())
                .topicCode(createCommand.getTopicCode())
                .imageUrl(createCommand.getImageUrl())
                .duration(createCommand.getDuration())
                .publishedDate(createCommand.getPublishedDate())
                .expiryDate(createCommand.getExpiryDate())
                .publisher(createCommand.getPublisher())
                .resourceUrl(createCommand.getResourceUrl())
                .level(createCommand.getLevel())
                .status(Status.SUBMITTED)
                .tags(createCommand.getTags())
                .title(createCommand.getTitle())
                .type(createCommand.getType())
                .views(0)
                .recommendations(0)
                .bookmarks(0)
                .keyPoints(createCommand.getKeyPoints())
                .build();

        capsule.setAddedOn(createCommand.getExecOn());
        capsule.setUpdatedOn(createCommand.getExecOn());
        capsule.setAddedBy(createCommand.getExecBy().getUserId());

        capsuleDynamoRepository.save(capsule);
    }

    @Override
    public void update(UpdateCommand updateCommand) {

        log.info(String.format("Entering update capsule service - Capsule Id:%s", updateCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(updateCommand.getCapsuleId());
        if (capsule != null) {

            capsule.setAudience(updateCommand.getAudience());
            capsule.setAuthor(updateCommand.getAuthor());
            capsule.setDescription(updateCommand.getDescription());
            capsule.setTopicCode(updateCommand.getTopicCode());
            capsule.setPublishedDate(updateCommand.getPublishedDate());
            capsule.setTitle(updateCommand.getTitle());
            capsule.setImageUrl(updateCommand.getImageUrl());
            capsule.setDuration(updateCommand.getDuration());
            capsule.setAuthor(updateCommand.getAuthor());
            capsule.setTags(updateCommand.getTags());
            capsule.setPublisher(updateCommand.getPublisher());
            capsule.setResourceUrl(updateCommand.getResourceUrl());
            capsule.setType(updateCommand.getType());
            capsule.setAudience(updateCommand.getAudience());
            capsule.setExpiryDate(updateCommand.getExpiryDate());
            capsule.setLevel(updateCommand.getLevel());
            capsule.setKeyPoints(updateCommand.getKeyPoints());
            capsule.setUpdatedOn(updateCommand.getExecOn());
            capsule.setUpdatedBy(updateCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }

    @Override
    public void disable(DisableCommand disableCommand) {

        log.info(String.format("Entering disable capsule service -  Capsule Id:%s", disableCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(disableCommand.getCapsuleId());
        if (capsule != null) {
            capsule.setStatus(Status.INACTIVE);

            capsule.setUpdatedOn(disableCommand.getExecOn());
            capsule.setUpdatedBy(disableCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }

    @Override
    public List<Capsule> getMyFeed(List<String> subscribedTopics) {
        log.info("Entering getMyFeed service");

        return capsuleDynamoRepository.findAllFeeds(subscribedTopics);
    }

    @Override
    public List<Capsule> getPendingApproval() {
        log.info("Entering getPendingApproval service");

        return capsuleDynamoRepository.findAllPendingApproval();
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        log.info("Entering getMetadata service");
        Map<String, List<String>> metadata = new HashMap<>();
        metadata.put("capsuleType", Arrays.stream(CapsuleType.values()).map(f -> f.toString()).collect(Collectors.toList()));
        metadata.put("publisher", Arrays.stream(Publisher.values()).map(f -> f.toString()).collect(Collectors.toList()));
        metadata.put("topicLevel", Arrays.stream(TopicLevel.values()).map(f -> f.toString()).collect(Collectors.toList()));
        metadata.put("targetAudience", Arrays.stream(TargetAudience.values()).map(f -> f.toString()).collect(Collectors.toList()));
        metadata.put("expiryInterval", Arrays.stream(ExpiryInterval.values()).map(f -> f.toString()).collect(Collectors.toList()));
        return metadata;
    }

    @Override
    public void approve(ApproveCommand approveCommand) {
        log.info(String.format("Entering approve capsule service -  Capsule Id:%s", approveCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(approveCommand.getCapsuleId());
        if (capsule != null) {
            capsule.setStatus(Status.ACTIVE);

            capsule.setUpdatedOn(approveCommand.getExecOn());
            capsule.setUpdatedBy(approveCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }

    @Override
    public void view(ViewCommand viewCommand) {
        log.info(String.format("Entering view capsule service -  Capsule Id:%s", viewCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(viewCommand.getCapsuleId());
        if (capsule != null) {
            Integer views = capsule.getViews();
            views += 1;
            capsule.setViews(views);
            capsule.setUpdatedOn(viewCommand.getExecOn());
            capsule.setUpdatedBy(viewCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }

    @Override
    public void addBookMark(AddBookmarkCommand addBookmarkCommand) {
        log.info(String.format("Entering addBookmark capsule service -  Capsule Id:%s", addBookmarkCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(addBookmarkCommand.getCapsuleId());

        if (capsule != null) {
            Integer bookmarkCount = capsule.getBookmarks();
            bookmarkCount += 1;
            capsule.setBookmarks(bookmarkCount);

            capsule.setUpdatedOn(addBookmarkCommand.getExecOn());
            capsule.setUpdatedBy(addBookmarkCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }

    @Override
    public void recommend(RecommendCommand recommendCommand) {
        log.info(String.format("Entering recommend capsule service -  Capsule Id:%s", recommendCommand.getCapsuleId()));

        Capsule capsule = capsuleDynamoRepository.findBy(recommendCommand.getCapsuleId());
        if (capsule != null) {
            Integer recommendationsCount = capsule.getRecommendations();
            recommendationsCount += 1;
            capsule.setRecommendations(recommendationsCount);

            capsule.setUpdatedOn(recommendCommand.getExecOn());
            capsule.setUpdatedBy(recommendCommand.getExecBy().getUserId());

            capsuleDynamoRepository.save(capsule);
        }
    }


    @Override
    public Capsule findBy(String capsuleId) {

        log.info(String.format("Entering find by capsule service - Capsule Id:%s", capsuleId));

        return capsuleDynamoRepository.findBy(capsuleId);
    }

    @Override
    public List<Capsule> findByTopic(String topicCode) {
        log.info("Entering findBy topic service");

        return capsuleDynamoRepository.findAllByTopicCode(topicCode);
    }


    @Override
    public List<Capsule> findAll() {

        log.info("Entering findAll capsule service");

        return capsuleDynamoRepository.findAll();
    }

    @Override
    public int getAllCapsulesCount() {
        log.info("Entering get all capsules count service");
        return capsuleDynamoRepository.getAllCapsulesCount();
    }

}
