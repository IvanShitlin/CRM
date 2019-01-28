import { Moment } from 'moment';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface IDebtorView {
    id?: number;
    mentorFirstName?: string;
    mentorLastName?: string;
    clientFirstName?: string;
    clientLastName?: string;
    courseName?: string;
    paymentDate?: Moment;
    sum?: Money;
}

export class DebtorView implements IDebtorView {
    constructor(
        public id?: number,
        public mentorFirstName?: string,
        public mentorLastName?: string,
        public clientFirstName?: string,
        public clientLastName?: string,
        public courseName?: string,
        public paymentDate?: Moment,
        public sum?: Money
    ) {
        this.sum = new Money(undefined, Currency.UAH);
    }
}
