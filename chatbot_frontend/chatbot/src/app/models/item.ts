export class Item {
    id: string;
    name: string;
    image: string;
    url: string;


    constructor($id: string, $name: string, $image: string, $url: string) {
        this.id = $id;
        this.name = $name;
        this.image = $image;
        this.url = $url;
    }

}
