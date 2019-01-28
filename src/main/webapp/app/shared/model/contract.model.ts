import { Moment } from 'moment';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export const enum CloseType {
    FROZEN = 'FROZEN',
    COMPLETED = 'COMPLETED',
    REFUSED = 'REFUSED',
    REJECTED = 'REJECTED'
}

export interface IContract {
    id?: number;
    startDate?: Moment;
    endDate?: Moment;
    firstPayDate?: Moment;
    nextPayDate?: Moment;
    closeType?: CloseType;
    mentorId?: number;
    mentorFirstName?: string;
    mentorLastName?: string;
    agreementId?: number;
    price?: Money;
    mentorRate?: Money;
    clientLastName?: string;
    courseName?: string;
}

export class Contract implements IContract {
    constructor(
        public id?: number,
        public startDate?: Moment,
        public endDate?: Moment,
        public firstPayDate?: Moment,
        public nextPayDate?: Moment,
        public closeType?: CloseType,
        public mentorId?: number,
        public mentorFirstName?: string,
        public mentorLastName?: string,
        public agreementId?: number,
        public price?: Money,
        public mentorRate?: Money,
        public clientLastName?: string,
        public courseName?: string
    ) {
        this.mentorRate = new Money(undefined, Currency.UAH);
        this.price = new Money(undefined, Currency.UAH);
    }
}
