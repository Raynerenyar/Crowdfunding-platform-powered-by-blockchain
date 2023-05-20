export class Url {

    url: string = ""

    add(path: string) { this.url += path; return this }
    getUrl() { return this.url }
}