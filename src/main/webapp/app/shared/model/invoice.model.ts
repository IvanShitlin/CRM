import { Moment } from 'moment';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface IInvoice {
    id?: number;
    dateFrom?: Moment;
    dateTo?: Moment;
    contractId?: number;
    sum?: Money;
    paymentId?: number;
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public dateFrom?: Moment,
        public dateTo?: Moment,
        public contractId?: number,
        public sum?: Money,
        public paymentId?: number
    ) {
        this.sum = new Money(undefined, Currency.UAH);
    }
}
