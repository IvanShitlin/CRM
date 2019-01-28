import { Moment } from 'moment';
import { ISalaryItem } from 'app/shared/model//salary-item.model';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface ISalary {
    id?: number;
    dateFrom?: Moment;
    dateTo?: Moment;
    paid?: boolean;
    paidDate?: Moment;
    mentorId?: number;
    mentorFirstName?: string;
    mentorLastName?: string;
    items?: ISalaryItem[];
    sum?: Money;
}

export class Salary implements ISalary {
    constructor(
        public id?: number,
        public dateFrom?: Moment,
        public dateTo?: Moment,
        public paid?: boolean,
        public paidDate?: Moment,
        public mentorId?: number,
        public mentorFirstName?: string,
        public mentorLastName?: string,
        public items?: ISalaryItem[],
        public sum?: Money
    ) {
        this.paid = false;
        this.sum = new Money(undefined, Currency.UAH);
    }
}
