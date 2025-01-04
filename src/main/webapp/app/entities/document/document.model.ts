import { IUserConfig } from 'app/entities/user-config/user-config.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';

export interface IDocument {
  id: number;
  title?: string | null;
  content?: string | null;
  documentType?: keyof typeof DocumentType | null;
  userConfig?: IUserConfig | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
