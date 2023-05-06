import { Injectable } from '@angular/core';
import Dexie, { Table } from 'dexie';
import { RequestDetails } from '../model/model';

@Injectable({
  providedIn: 'root'
})
export class DexieDBService extends Dexie {

  requests!: Dexie.Table<RequestDetails, number>

  constructor() {
    super('requests');
    this.version(1).stores({
      requests: 'requestId'
    })
    // this.request = this.table('request')
  }
}

// this line is important
export const db = new DexieDBService()
