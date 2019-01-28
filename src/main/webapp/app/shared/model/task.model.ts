import { Moment } from 'moment';

export const enum TaskType {
    CONTACT = 'CONTACT',
    TEST_PERIOD = 'TEST_PERIOD',
    MENTOR_CHANGE = 'MENTOR_CHANGE',
    QUESTIONS = 'QUESTIONS'
}

export const enum TaskPriority {
    URGENT = 'URGENT',
    CRITICAL = 'CRITICAL',
    IMPORTANT = 'IMPORTANT',
    NORMAL = 'NORMAL',
    CAN_WAIT = 'CAN_WAIT'
}

export interface ITask {
    id?: number;
    date?: Moment;
    description?: string;
    type?: TaskType;
    priority?: TaskPriority;
    closed?: boolean;
    userId?: number;
    userFirstName?: string;
    userLastName?: string;
}

export class Task implements ITask {
    constructor(
        public id?: number,
        public date?: Moment,
        public description?: string,
        public type?: TaskType,
        public priority?: TaskPriority,
        public closed?: boolean,
        public userId?: number,
        public userFirstName?: string,
        public userLastName?: string
    ) {
        this.closed = this.closed || false;
    }
}
