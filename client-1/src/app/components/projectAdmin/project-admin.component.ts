import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-project-admin',
  templateUrl: './project-admin.component.html',
  styleUrls: ['./project-admin.component.css']
})
export class ProjectAdminComponent {
  newProjectForm!: FormGroup
  constructor(private fb: FormBuilder) { }
  ngOnInit(): void {
    this.newProjectForm = this.fb.group({
      goal: this.fb.control(null, [Validators.required]),
      deadline: this.fb.control(null, [Validators.required]),
      tokenAddress: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required])
    })

  }

  createProject() {
    let goal = this.newProjectForm.get('goal')?.value
    let dueDate = this.newProjectForm.get('deadline')?.value
    let tokenAddress = this.newProjectForm.get('tokenAddress')?.value
    let description = this.newProjectForm.get('description')?.value
    let deadline = Date.parse(dueDate)
    // this.bcSvc.createProject(goal, deadline, tokenAddress, description)
  }
}
