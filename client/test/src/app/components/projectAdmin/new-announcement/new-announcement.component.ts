import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Editor, Toolbar } from 'ngx-editor';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { Announcement } from "../../../model/model";
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';

@Component({
  selector: 'app-new-announcement',
  templateUrl: './new-announcement.component.html',
  styleUrls: ['./new-announcement.component.css']
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
  hasUpdated = false
  editing = false
  editedAnnouncement!: Announcement

  constructor(private fb: FormBuilder, private mongoSvc: MongoRepoService, private route: ActivatedRoute, private storageSvc: SessionStorageService, private router: Router, private msgSvc: PrimeMessageService) { }

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
        console.log('new announcement')
        if (param.keys.includes('edit')) {
          let index = parseInt(param.get('edit')!)
          this.editing = true
          this.editedAnnouncement = this.mongoSvc.announcements[index]
          this.editorForm.get('editor')?.setValue(this.mongoSvc.announcements[index].body)
        }
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
      if (this.editing) {

        this.editedAnnouncement.body = textBody
        this.editedAnnouncement.datetimeEdited = datetime

        this.mongoSvc.editAnnouncement(this.editedAnnouncement)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: () => {
              this.msgSvc.generalSuccessMethod("Announcement edit submitted!")
              this.router.navigateByUrl(`/project-admin/${this.projectAddress}/announcements`)
            },
            error: () => this.msgSvc.generalErrorMethod("Announcement edit failed to submit!")
          })
      } else {
        this.mongoSvc.insertAnnouncement(announcement)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: (result: boolean) => {
              this.msgSvc.generalSuccessMethod("Announcement posted!")
              this.router.navigateByUrl(`/project-admin/${this.projectAddress}/announcements`)
            },
            error: (result: boolean) => this.msgSvc.generalErrorMethod("Announcement failed to post!")
          })
      }
    }
  }
}
