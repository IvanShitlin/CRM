import { Moment } from 'moment';
import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface IPayment {
    id?: number;
    date?: Moment;
    invoiceId?: number;
    sum?: Money;
}

export class Payment implements IPayment {
    constructor(public id?: number, public date?: Moment, public invoiceId?: number, public sum?: Money) {
        this.sum = new Money(undefined, Currency.UAH);
    }
}
