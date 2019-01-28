package ru.shitlin.repository.view;

import ru.shitlin.domain.view.MentorStudentsView;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MentorStudentsView entity.
 */
@Repository
public interface MentorViewRepository extends ViewRepository<MentorStudentsView, Long> {

}
