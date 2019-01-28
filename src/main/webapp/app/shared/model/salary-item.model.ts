import { Moment } from 'moment';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface ISalaryItem {
    id?: number;
    dateFrom?: Moment;
    dateTo?: Moment;
    accounted?: boolean;
    mentorId?: number;
    mentorFirstName?: string;
    mentorLastName?: string;
    invoiceId?: number;
    salaryId?: number;
    sum?: Money;
}

export class SalaryItem implements ISalaryItem {
    constructor(
        public id?: number,
        public dateFrom?: Moment,
        public dateTo?: Moment,
        public accounted?: boolean,
        public mentorId?: number,
        public mentorFirstName?: string,
        public mentorLastName?: string,
        public invoiceId?: number,
        public salaryId?: number,
        public sum?: Money
    ) {
        this.accounted = false;
        this.sum = new Money(undefined, Currency.UAH);
    }
}
