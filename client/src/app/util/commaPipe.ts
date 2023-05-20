import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'commaSeparated'
})
export class CommaSeparatedPipe implements PipeTransform {
    transform(value: number | bigint): string {
        if (value == null) return '';

        const stringValue = String(value);

        // Split the string into parts before and after the decimal point (if any)
        const parts = stringValue.split('.');
        const integerPart = parts[0];
        const decimalPart = parts[1] ? '.' + parts[1] : '';

        // Insert commas every three digits in the integer part
        const formattedInteger = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

        return formattedInteger + decimalPart;
    }
}