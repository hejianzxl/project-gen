package ${groupId}.dao.mapper;

import ${groupId}.dao.model.Demo;
import ${groupId}.dao.model.DemoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

public interface DemoMapper {
    @SelectProvider(type=DemoSqlProvider.class, method="countByExample")
    long countByExample(DemoExample example);

    @DeleteProvider(type=DemoSqlProvider.class, method="deleteByExample")
    int deleteByExample(DemoExample example);

    @Delete({
        "delete from t_demo",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into t_demo (name, version, ",
        "create_time, update_time)",
        "values (#{name,jdbcType=VARCHAR}, #{version,jdbcType=INTEGER}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ROWID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Demo record);

    @InsertProvider(type=DemoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ROWID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(Demo record);

    @SelectProvider(type=DemoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Demo> selectByExampleWithRowbounds(DemoExample example, RowBounds rowBounds);

    @SelectProvider(type=DemoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Demo> selectByExample(DemoExample example);

    @Select({
        "select",
        "id, name, version, create_time, update_time",
        "from t_demo",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Demo selectByPrimaryKey(Long id);

    @UpdateProvider(type=DemoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Demo record, @Param("example") DemoExample example);

    @UpdateProvider(type=DemoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Demo record, @Param("example") DemoExample example);

    @UpdateProvider(type=DemoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Demo record);

    @Update({
        "update t_demo",
        "set name = #{name,jdbcType=VARCHAR},",
          "version = #{version,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Demo record);
}