import { Moment } from 'moment';
import { ICourseType } from 'app/shared/model/course-type.model';

export const enum AgreementStatus {
    NEW = 'NEW',
    INTERVIEW = 'INTERVIEW',
    WAITING = 'WAITING',
    ACTIVE = 'ACTIVE',
    FROZEN = 'FROZEN',
    COMPLETED = 'COMPLETED',
    REFUSED = 'REFUSED',
    REJECTED = 'REJECTED'
}

export interface IAgreement {
    id?: number;
    startDate?: Moment;
    endDate?: Moment;
    status?: AgreementStatus;
    clientId?: number;
    courseId?: number;
    courseName?: string;
    clientLastName?: string;
    clientFirstName?: string;
    courseType?: ICourseType;
}

export class Agreement implements IAgreement {
    constructor(
        public id?: number,
        public startDate?: Moment,
        public endDate?: Moment,
        public status?: AgreementStatus,
        public clientId?: number,
        public courseId?: number,
        public courseName?: string,
        public clientLastName?: string,
        public clientFirstName?: string,
        public courseType?: ICourseType
    ) {}
}
