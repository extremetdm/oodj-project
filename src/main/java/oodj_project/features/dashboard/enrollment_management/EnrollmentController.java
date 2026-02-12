package oodj_project.features.dashboard.enrollment_management;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.class_management.ClassRepository;

public class EnrollmentController {

    private final Session session;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;

    public EnrollmentController(
        Session session,
        EnrollmentRepository enrollmentRepository,
        ClassRepository classRepository
    ) {
        this.session = session;
        this.enrollmentRepository = enrollmentRepository;
        this.classRepository = classRepository;
    }

    public PaginatedResult<ClassGroup> upcomingClasses(Query<ClassGroup> query) {
        var enrollments = enrollmentRepository.all();

        var classOccupancy = enrollments
            .stream()
            .collect(Collectors.groupingBy(Enrollment::classGroup, Collectors.counting()));

        var enrolledClasses = enrollments
            .stream()
            .filter(enrollment -> enrollment.student() == session.currentUser())
            .map(Enrollment::classGroup)
            .collect(Collectors.toSet());

        var now = new Date();
        query = query.toBuilder()
            .addFilter(
                classGroup -> classGroup.startDate().before(now)
                    && classGroup.lecturer() != null
                    && !enrolledClasses.contains(classGroup)
                    && classGroup.maxCapacity() > classOccupancy.getOrDefault(classGroup, 0l)
            )
            .build();

        return classRepository.get(query);
    }

    public PaginatedResult<Enrollment> index(Query<Enrollment> query) {
        query = query.toBuilder()
            .addFilter(enrollment -> enrollment.student() == session.currentUser())
            .build();

        return enrollmentRepository.get(query);
    }

    public synchronized void create(Enrollment enrollment) throws IOException {
        enrollmentRepository.create(enrollment);
    }

    public synchronized void unenroll(Enrollment enrollment) throws IOException {
        var now = new Date();
        var classGroup = enrollment.classGroup();

        if (classGroup.startDate().before(now)) {
            enrollmentRepository.delete(enrollment);
        } else if (classGroup.endDate().before(now)) {
            enrollmentRepository.update(enrollment.id(), enrollment.withDropoutDate(now));
        } else {
            throw new IllegalArgumentException("Cannot unenroll completed classes.");
        }
    }
}