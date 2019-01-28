import { Currency } from 'app/shared/model/currency.model';

export interface IMoney {
    amount?: number;
    currency?: Currency;
}

export class Money implements IMoney {
    constructor(public amount?: number, public currency?: Currency) {}
}
