import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Editor, Toolbar } from 'ngx-editor';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { Announcement } from "../../../model/model";
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-announcement-editor',
  templateUrl: './announcement-editor.component.html',
  styleUrls: ['./announcement-editor.component.css']
})
export class AnnouncementEditorComponent implements OnInit, OnDestroy {
  toolbar: Toolbar = [
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['code', 'blockquote'],
    ['ordered_list', 'bullet_list'],
    [{ heading: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'] }],
    ['link', 'image'],
    ['text_color', 'background_color'],
    ['align_left', 'align_center', 'align_right', 'align_justify'],
  ];

  editor!: Editor
  projectAddress!: string | null
  creatorAddress!: string
  editorForm!: FormGroup
  notifier$ = new Subject<boolean>()
  hasPosted = false

  constructor(private fb: FormBuilder, private mongo: MongoRepoService, private route: ActivatedRoute, private storageSvc: SessionStorageService) { }

  ngOnInit(): void {
    // might need to put protection against unauthorised access to user-access dashboard
    if (this.storageSvc.isLoggedIn()) {
      this.creatorAddress = this.storageSvc.getUser().username
    }

    this.editor = new Editor()
    this.editorForm = this.fb.group({
      editor: this.fb.control<string>('', Validators.required)
    })
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        this.projectAddress = param.get('address')
      })


  }

  ngOnDestroy(): void {
    this.editor.destroy()
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

  onSubmit() {
    let textBody = this.editorForm.get('editor')?.value
    console.log(this.creatorAddress)
    if (this.projectAddress) {
      let datetime = new Date()
      let announcement: Announcement = {
        projectAddress: this.projectAddress,
        creatorAddress: this.creatorAddress,
        datetimePosted: datetime,
        body: textBody
      }
      this.mongo.insertAnnouncement(announcement)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (result: boolean) => { this.hasPosted = result; console.log(result) },
          error: (result: boolean) => { this.hasPosted = result; console.log(result) }
        })
    }
  }
}
