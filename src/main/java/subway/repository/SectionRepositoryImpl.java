package subway.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.Section;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private static final Map<Long, Section> store = new HashMap<>();

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
        init();
    }

    @Override
    public Section insert(Section section) {
        Section storedSection = sectionDao.insert(section);
        store.put(storedSection.getId(), storedSection);
        return storedSection;
    }

    @Override
    public List<Section> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Section> findAllByLineId(final Long lineId) {
        return store.values().stream()
                .filter(section -> section.getLineId().equals(lineId))
                .collect(Collectors.toUnmodifiableList());
    }

    private void init() {
        List<Section> sections = sectionDao.findAll();
        for (Section section : sections) {
            store.put(section.getId(), section);
        }
    }

    @Override
    public List<Section> findSectionByLineIdAndStationId(final Long lineId, final Long stationId) {
        return store.values().stream()
                .filter(section -> section.getLineId().equals(lineId))
                .filter(section -> section.getUpStationId().equals(stationId) ||
                        section.getDownStationId().equals(stationId))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int countByLineId(final Long lineId) {
        return (int) store.values().stream()
                .filter(section -> section.getLineId().equals(lineId))
                .count();
    }

    @Override
    public void update(Section section) {
        sectionDao.update(section);
        store.put(section.getId(), section);
    }

    @Override
    public void deleteById(Long id) {
        sectionDao.deleteById(id);
        store.remove(id);
    }

    @Override
    public void deleteAllByLineId(final Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId().equals(lineId))
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            store.remove(id);
        }
    }

    @Override
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId().equals(lineId))
                .filter(section -> section.getUpStationId().equals(stationId)
                        || section.getDownStationId().equals(stationId))
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            sectionDao.deleteById(id);
        }
        for (Long id : ids) {
            store.remove(id);
        }
    }

    @Override
    public void deleteAll() {
        sectionDao.deleteAll();
        store.clear();
    }
}
