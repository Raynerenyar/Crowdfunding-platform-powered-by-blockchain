import { AfterViewInit, Component, ElementRef, Input, Output, Renderer2 } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Editor, Toolbar } from 'ngx-editor';
import { Subject, takeUntil } from 'rxjs';
import { NewComment } from 'src/app/model/model';
import { MongoRepoService } from 'src/app/services/mongo.repo.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-new-comment',
  templateUrl: './new-comment.component.html',
  styleUrls: ['./new-comment.component.css']
})
export class NewCommentComponent implements AfterViewInit {
  toolbar: Toolbar = [
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['code', 'blockquote'],
    ['ordered_list', 'bullet_list'],
    ['link'],
    ['text_color'],
    ['align_left', 'align_center', 'align_right', 'align_justify'],
  ];
  editor!: Editor;
  commentForm!: FormGroup
  notifier$ = new Subject<boolean>()
  @Input()
  projectAddress!: string
  @Output()
  onSubmission = new Subject()

  constructor(private fb: FormBuilder, private elementRef: ElementRef, private mongoSvc: MongoRepoService, private storageSvc: SessionStorageService, private msgSvc: PrimeMessageService) { }

  ngOnInit(): void {
    this.editor = new Editor();
    window.scrollTo(0, document.body.scrollHeight);
    this.commentForm = this.fb.group({
      comment: this.fb.control<string>('', [Validators.required])
    })
  }

  ngAfterViewInit(): void {
    const element = this.elementRef.nativeElement.querySelector('#commentForm');
    element.scrollIntoView({ behavior: 'smooth' });
  }

  onSubmit() {
    let posterAddress = this.storageSvc.getAddress()
    console.log(this.projectAddress)
    if (posterAddress) {
      let commentBody = this.commentForm.get('comment')?.value
      let textBody: NewComment = {
        projectAddress: this.projectAddress,
        posterAddress: posterAddress,
        body: commentBody,
        datetimePosted: new Date()
      }

      // send to mongo
      this.mongoSvc.insertComment(textBody)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: () => { this.onSubmission.next(true) },
          error: () => { }
        })
    } else {
      this.msgSvc.generalErrorMethod("You have to login to post a comment.")
    }
  }

  ngOnDestroy(): void {
    this.editor.destroy();
  }
}
