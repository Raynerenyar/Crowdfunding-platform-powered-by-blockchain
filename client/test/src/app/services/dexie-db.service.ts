import { Injectable } from '@angular/core';
import Dexie, { Table } from 'dexie';
import { Request } from '../model/model';

@Injectable({
  providedIn: 'root'
})
export class DexieDBService extends Dexie {

  requests!: Dexie.Table<Request, number>

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
