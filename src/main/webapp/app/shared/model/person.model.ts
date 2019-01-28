export interface IPerson {
    id?: number;
    firstName?: string;
    lastName?: string;
    patronymic?: string;
    phone?: string;
    email?: string;
    skype?: string;
    country?: string;
    city?: string;
}

export class Person implements IPerson {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public patronymic?: string,
        public phone?: string,
        public email?: string,
        public skype?: string,
        public country?: string,
        public city?: string
    ) {}
}
