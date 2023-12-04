package com.tekcapzule.feed.domain.client;

import com.tekcapzule.feed.domain.exception.FeedCreationException;

public interface UrlService {

    UrlMetaTag extractDetails(String url);
    byte[] downloadImage(String url,String imageName) throws FeedCreationException;
}
