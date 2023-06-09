package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import org.oj.server.dao.TagRepository;
import org.oj.server.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class TagService {
    public static final Map<String, Tag> tagMap = new HashMap<>();
    @Autowired
    private TagRepository tagRepository;


    @PostConstruct
    public void init() {
        // 预加载
        List<Tag> all = tagRepository.findAll();
        all.forEach(tag -> tagMap.put(tag.getId(), tag));
    }
}
