import { NewCommentComponent } from "./comments/new-comment/new-comment.component";
import { CommentsComponent } from "./comments/view-comments/comments.component";
import { ExploreProjectsComponent } from "./explore-projects/explore-projects.component";
import { ProjectBodyComponent } from "./project/project-body/project-body.component";
import { ProjectMainComponent } from "./project/project-main/project-main.component";
import { ProjectHeaderComponent } from "./project/projectHeader/project-header.component";
import { ViewAnnouncementComponent } from "./viewAnnouncements/view-announcement.component";
import { RequestComponent } from "./viewRequest/request/request.component";
import { RequestListComponent } from "./viewRequest/request.list.component";



export const exploreComponents = [
    ExploreProjectsComponent,
    RequestListComponent,
    RequestComponent,
    ProjectMainComponent,
    ProjectHeaderComponent,
    ProjectBodyComponent,
    ViewAnnouncementComponent,
    CommentsComponent,
    NewCommentComponent,
]