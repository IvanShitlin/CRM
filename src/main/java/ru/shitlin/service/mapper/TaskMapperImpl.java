package ru.shitlin.service.mapper;

import ru.shitlin.domain.Task;
import ru.shitlin.domain.User;
import ru.shitlin.service.dto.TaskDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-01-06T15:43:09+0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_121 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Task> toEntity(List<TaskDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Task> list = new ArrayList<Task>( dtoList.size() );
        for ( TaskDTO taskDTO : dtoList ) {
            list.add( toEntity( taskDTO ) );
        }

        return list;
    }

    @Override
    public List<TaskDTO> toDto(List<Task> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TaskDTO> list = new ArrayList<TaskDTO>( entityList.size() );
        for ( Task task : entityList ) {
            list.add( toDto( task ) );
        }

        return list;
    }

    @Override
    public TaskDTO toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();

        Long id = taskUserId( task );
        if ( id != null ) {
            taskDTO.setUserId( id );
        }
        taskDTO.setCreatedBy( task.getCreatedBy() );
        taskDTO.setCreatedDate( task.getCreatedDate() );
        taskDTO.setLastModifiedBy( task.getLastModifiedBy() );
        taskDTO.setLastModifiedDate( task.getLastModifiedDate() );
        taskDTO.setId( task.getId() );
        taskDTO.setDate( task.getDate() );
        taskDTO.setDescription( task.getDescription() );
        taskDTO.setType( task.getType() );
        taskDTO.setPriority( task.getPriority() );
        taskDTO.setClosed( task.isClosed() );

        return taskDTO;
    }

    @Override
    public Task toEntity(TaskDTO taskDTO) {
        if ( taskDTO == null ) {
            return null;
        }

        Task task = new Task();

        task.setUser( userMapper.userFromId( taskDTO.getUserId() ) );
        task.setCreatedBy( taskDTO.getCreatedBy() );
        task.setCreatedDate( taskDTO.getCreatedDate() );
        task.setLastModifiedBy( taskDTO.getLastModifiedBy() );
        task.setLastModifiedDate( taskDTO.getLastModifiedDate() );
        task.setId( taskDTO.getId() );
        task.setDate( taskDTO.getDate() );
        task.setDescription( taskDTO.getDescription() );
        task.setType( taskDTO.getType() );
        task.setPriority( taskDTO.getPriority() );
        task.setClosed( taskDTO.isClosed() );

        return task;
    }

    private Long taskUserId(Task task) {
        if ( task == null ) {
            return null;
        }
        User user = task.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
