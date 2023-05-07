import { Component } from '@angular/core';

@Component({
  selector: 'app-announcemnt-editor',
  templateUrl: './announcemnt-editor.component.html',
  styleUrls: ['./announcemnt-editor.component.css']
})
export class AnnouncemntEditorComponent {
  text!: string;

  // quill = new Quill('#editor', {
  //   theme: 'snow'
  // });
}
