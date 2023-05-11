import { NewCommentComponent } from "./comments/new-comment/new-comment.component";
import { CommentsComponent } from "./comments/view-comments/comments.component";
import { ExploreProjectsComponent } from "./explore-projects/explore-projects.component";
import { ProjectBodyComponent } from "./project/project-body/project-body.component";
import { ProjectMainComponent } from "./project/project-main/project-main.component";
import { ProjectHeaderComponent } from "./project/projectHeader/project-header.component";
import { ViewAnnouncementComponent } from "./viewAnnouncements/view-announcement.component";
import { ContributeRequestComponent } from "./viewRequest/contribute-request/contribute-request.component";
import { RequestComponent } from "./viewRequest/request.component";



export const exploreComponents = [
    ExploreProjectsComponent,
    RequestComponent,
    ContributeRequestComponent,
    ProjectMainComponent,
    ProjectHeaderComponent,
    ProjectBodyComponent,
    ViewAnnouncementComponent,
    CommentsComponent,
    NewCommentComponent,
]