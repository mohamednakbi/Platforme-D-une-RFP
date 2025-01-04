import { Component, Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonModule, KeyValuePipe } from '@angular/common';
import { IReference } from '../reference/reference.model';
import { IContext } from '../context/context.model';
import { ICV } from '../cv/cv.model';
import { ITechnology } from '../technology/technology.model';

export interface Response {
  cvMap: { [key: string]: number };
  technologyMap: { [key: string]: number };
  references: IReference[];
  contexts: IContext[];
  // technologyMap:ITechnology[];
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:9000/api/response/generate-res';

  constructor(private http: HttpClient) {}

  getRandomResponse(): Observable<Response> {
    return this.http.get<Response>(this.apiUrl);
  }
}

@Component({
  selector: 'jhi-resultats-rfp',
  standalone: true,
  imports: [KeyValuePipe, CommonModule, HttpClientModule],
  providers: [],
  templateUrl: './resultats-rfp.component.html',
  styleUrls: ['./resultats-rfp.component.scss', '../../shared/table-scss-shared.scss'],
})
export class ResultatsRFPComponent implements OnInit {
  technologyArray: { key: string; value: number }[] = [];
  cvArray: { key: string; value: number }[] = [];

  response: Response | undefined;

  DataRfp = [
    {
      reference: 'cart1',
      context: 'description',
      cv: ['gg', 'hfhgh', 'hgfhfh'],
      technology: ['java', 'springboot'],
      value: '0o327226296516',
    },
    {
      reference: 'cart1',
      context: 'description',
      cv: ['gg', 'hfhgh', 'hgfhfh'],
      technology: ['java', 'springboot'],
      value: '69267565253207',
    },
  ];

  constructor(private responseService: ApiService) {}

  ngOnInit(): void {
    this.loadResponse();
  }

  loadResponse(): void {
    this.responseService.getRandomResponse().subscribe(
      data => {
        this.response = data;
        this.technologyArray = Object.entries(data.technologyMap).map(([key, value]) => ({ key, value }));
        this.cvArray = Object.entries(data.cvMap).map(([key, value]) => ({ key, value }));

        console.log('Parsed response:', data);

        console.log('CV array:', this.cvArray);
        console.warn('Technology array:', this.technologyArray);
        //console.log("ffff",Object.keys(this.technologyArray).map((key)=>key))
      },
      error => {
        console.error('Error fetching response data', error);
      },
    );
  }

  extractTechnologyDetails(key: string): { name: string } | null {
    const regex = /name='([^']+)'/;
    const match = key.match(regex);
    console.log('match', match);

    if (match && match.length > 1) {
      return { name: match[1] };
    } else {
      return null; // Retourne null si la chaîne ne correspond pas au format attendu
    }
  }

  // formatize(key: string): { name: string } | null {
  //   const regex = /name='([^']+)'/;
  //   const match = key.match(regex);
  //   console.log("match",match?.[0])
  //   if (match && match.length > 1) {
  //     return { name: match[1] };
  //   } else {
  //     return null;
  //   }
  // }

  formatizeCV(data: string): string | null {
    const titleMatch = data.match(/title='([^']+)'/);
    const title = titleMatch && titleMatch.length > 1 ? titleMatch[1] : '';
    // const ContentMatch = data.match(/content='([^']+)'/);
    // const Content = ContentMatch && ContentMatch.length > 1 ? ContentMatch[1] : '';

    //   const userConfigMatch = data.match(/username='([^']+)'/);
    // const username = userConfigMatch && userConfigMatch.length > 1 ? userConfigMatch[1] : '';

    if (title) {
      return `${title}   `;
    } else if (title) {
      return title;
      // } else if (username) {
      // return username;
      // } else if (Content) {
      //   return Content;
    } else {
      return null;
    }
  }
  formatizeTEchno(data: string): string {
    console.log('data>>', data);
    const nameMatch = data.match(/name='([^']+)'/);
    console.log('nameMatch>>>>', nameMatch);

    if (nameMatch && nameMatch.length > 1) {
      return nameMatch[1];
    }
    return '';
  }
}
