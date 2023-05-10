package subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class StationDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );
    
    
    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }
    
    public Station insert(final Station station) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        final Long id = this.insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }
    
    public List<Station> findAll() {
        final String sql = "select * from STATION";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }
    
    public Station findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.rowMapper, id);
    }
    
    public void update(final Station newStation) {
        final String sql = "update STATION set name = ? where id = ?";
        this.jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }
    
    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        this.jdbcTemplate.update(sql, id);
    }
    
    public boolean hasStations(final long baseStationId, final long newStationId) {
        final String sql = "select count(*) from STATION where id = ? or id = ?";
        final Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, baseStationId, newStationId);
        return count == 2;
    }
}
