export class Item {
    title: string;
    imageUrl: string;
    url: string;

    constructor($title: string, $imageUrl: string, $url: string) {
        this.title = $title;
        this.imageUrl = $imageUrl;
        this.url = $url;
    }

}
