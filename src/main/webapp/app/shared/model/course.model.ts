import { IMentor } from 'app/shared/model//mentor.model';

export interface ICourse {
    id?: number;
    name?: string;
    imageContentType?: string;
    image?: any;
    description?: string;
    emailTemplate?: string;
    mentors?: IMentor[];
}

export class Course implements ICourse {
    constructor(
        public id?: number,
        public name?: string,
        public imageContentType?: string,
        public image?: any,
        public description?: string,
        public emailTemplate?: string,
        public mentors?: IMentor[]
    ) {}
}
