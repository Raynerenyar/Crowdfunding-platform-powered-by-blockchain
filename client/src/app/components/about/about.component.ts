import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent {

  arr = [
    { src: "https://docs.soliditylang.org/en/v0.8.17/_images/logo.svg", title: "Solidity" },
    { src: "../../../assets/icons/web3js.svg", title: "Web3.js" },
    { src: "https://avatars.githubusercontent.com/u/22208471?s=200&v=4", title: "Web3j" },
    { src: "https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png", title: "Java" },
    { src: "https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png", title: "Maven" },
    { src: "https://user-images.githubusercontent.com/25181517/117201470-f6d56780-adec-11eb-8f7c-e70e376cfd07.png", title: "Spring" },
    { src: "https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png", title: "Spring boot" },
    { src: "https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png", title: "Mysql" },
    { src: "https://user-images.githubusercontent.com/25181517/182884177-d48a8579-2cd0-447a-b9a6-ffc7cb02560e.png", title: "Mongo" },
    { src: "https://user-images.githubusercontent.com/25181517/192158954-f88b5814-d510-4564-b285-dff7d6400dad.png", title: "Html" },
    { src: "https://user-images.githubusercontent.com/25181517/183898674-75a4a1b1-f960-4ea9-abcb-637170a00a75.png", title: "Css" },
    { src: "https://user-images.githubusercontent.com/25181517/183890595-779a7e64-3f43-4634-bad2-eceef4e80268.png", title: "Angular" },
    { src: "https://i0.wp.com/www.primefaces.org/wp-content/uploads/2018/05/primeng-logo.png?ssl=1", title: "Primeng" },
    { src: "https://user-images.githubusercontent.com/25181517/117447155-6a868a00-af3d-11eb-9cfe-245df15c9f3f.png", title: "Javascript" },
    { src: "https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png", title: "Typescript" },
    { src: "https://user-images.githubusercontent.com/25181517/117207330-263ba280-adf4-11eb-9b97-0ac5b40bc3be.png", title: "Docker" },
  ]
}
