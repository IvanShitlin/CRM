import { Money } from 'app/shared/model/money.model';
import { Currency } from 'app/shared/model/currency.model';

export interface ICourseType {
    id?: number;
    type?: string;
    location?: string;
    price?: Money;
    mentorRate?: Money;
}

export class CourseType implements ICourseType {
    constructor(public id?: number, public type?: string, public location?: string, public price?: Money, public mentorRate?: Money) {
        this.mentorRate = new Money(undefined, Currency.UAH);
        this.price = new Money(undefined, Currency.UAH);
    }
}
