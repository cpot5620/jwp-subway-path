package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.SectionWithStationNameEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;

@Repository
public class SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long save(final Long lineId, final Section section) {
        SectionEntity sectionEntity = toEntity(lineId, section);

        if (sectionDao.exists(sectionEntity)) {
            throw new DuplicateException(ErrorMessage.DUPLICATE_SECTION);
        }

        return sectionDao.save(sectionEntity);
    }

    public Section findByLineIdAndUpStationAndDownStation(final Station upStation,
                                                          final Station downStation,
                                                          final Long lineId) {
        SectionWithStationNameEntity sectionEntity = sectionDao.findByLineIdAndUpStationEntityAndDownStationEntity(
                        upStation.getId(),
                        downStation.getId(),
                        lineId
                )
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_SECTION));

        StationEntity upStationEntity = sectionEntity.getUpStationEntity();
        StationEntity downStationEntity = sectionEntity.getDownStationEntity();

        return new Section(
                new Station(upStationEntity.getId(), upStationEntity.getName()),
                new Station(downStationEntity.getId(), downStationEntity.getName()),
                sectionEntity.getDistance()
        );
    }

    public void delete(final Long lineId, final Section deletedSection) {
        int affectedRow = sectionDao.delete(toEntity(lineId, deletedSection));

        if (affectedRow == 0) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND_SECTION);
        }
    }

    public void deleteByLineId(final Long lineId) {
        int affectedRow = sectionDao.deleteByLineId(lineId);

        if (affectedRow == 0) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND_SECTION_BY_LINE_ID);
        }
    }

    private SectionEntity toEntity(final Long lineId, final Section section) {
        return new SectionEntity(
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                lineId,
                section.getDistance()
        );
    }
}
