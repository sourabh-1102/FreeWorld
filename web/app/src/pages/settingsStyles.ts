/*
 * Grouped inset rows, the settings idiom the app uses elsewhere: the
 * Extension, Settings, Discord and Listen Together pages.
 */

const row = 'relative flex items-center justify-between px-4 py-[11px] type-title-3 hairline-[16px]';
const integration = 'relative grid items-center gap-x-3 px-4 py-[11px] type-title-3 hairline-[52px]';
const field = 'w-[220px] flex-none rounded-[8px] border-0 bg-quaternary px-2.5 py-[5px] type-body text-primary';

export const settingsUi = {
  /** The page title and its rows as one column, centred in the page. */
  centered: 'mx-auto max-w-[calc(640px+2*var(--body-gutter))]',
  body: 'mx-(--body-gutter) mt-5 mb-[120px] max-w-[640px]',
  group: 'fw-card overflow-hidden rounded-[10px] bg-quinary',
  sectionHeader: 'mx-4 mt-7 mb-2 type-subhead-emphasized uppercase text-secondary',
  footer: 'mx-4 mt-2 type-callout text-secondary',

  row,
  /** A row holding a switch, a select or a field: the whole row is its label. */
  controlRow: `${row} cursor-pointer gap-4`,
  rowText: 'flex flex-col gap-0.5',
  rowDetail: 'type-callout text-secondary',
  value: 'inline-flex items-center gap-1.5 text-right type-body-reduced text-secondary',
  dot: 'size-2 rounded-full bg-green',

  /** Download rows: browser logo, title and detail, chevron. */
  download:
    'relative grid grid-cols-[28px_minmax(0,1fr)_auto] items-center gap-x-4 px-4 py-[11px] hover:bg-quinary hairline-[60px]',
  updateDot: 'size-2.5 justify-self-center rounded-full bg-key',
  downloadText: 'flex min-w-0 flex-col gap-0.5',
  downloadTitle: 'type-title-3-emphasized text-key',
  downloadDetail: 'type-body text-secondary',
  chevron: 'text-tertiary',
  steps: 'mx-4 mt-2.5 list-decimal ps-[18px] type-body-tall text-secondary [&>li+li]:mt-1',

  /** Integration rows: a coloured app-icon square, the name, the state. */
  integration: `${integration} grid-cols-[24px_minmax(0,1fr)_auto]`,
  /** An integration row without the icon square. */
  integrationPlain: `${integration} grid-cols-[minmax(0,1fr)_auto]`,
  integrationIcon: 'grid size-6 place-items-center rounded-[6px] text-white',
  integrationName: 'min-w-0',
  /** Settings: an integration row that opens a page of its own. */
  linkRow: 'w-full cursor-pointer text-start hover:bg-quaternary',
  /** A row's own action, after its status (Connect, Disconnect). */
  inlineButton: 'ml-2 rounded-[12px] bg-quaternary px-2.5 py-[3px] type-callout-emphasized text-primary disabled:opacity-50',
  inlineLink: 'text-key',

  switch: [
    'relative h-[22px] w-[38px] flex-none cursor-pointer appearance-none rounded-[11px] bg-quaternary transition-[background-color] duration-200 checked:bg-green',
    'after:absolute after:top-0.5 after:left-0.5 after:size-[18px] after:rounded-full after:bg-white after:shadow-[0_1px_3px_rgb(0_0_0/0.3)] after:transition-[translate] after:duration-200 checked:after:translate-x-4',
  ].join(' '),
  input: field,

  /** Appearance: theme cards, one radio each, grouped by the spec's sections. */
  themeGroup: 'fw-card overflow-hidden rounded-[10px] bg-quinary px-4 pb-4',
  themeHeader: '-mx-4 flex items-center justify-between border-b-[0.5px] border-divider px-4 py-[11px] type-title-3',
  themeSection: 'mt-3 mb-2 type-callout-emphasized text-secondary',
  themeGrid: 'grid grid-cols-[repeat(auto-fill,minmax(128px,1fr))] gap-2.5',
  themeCard: [
    'relative flex cursor-pointer flex-col gap-2 rounded-[10px] border-2 border-transparent bg-page p-1.5 type-body-emphasized text-primary',
    'hover:border-divider has-checked:border-key has-focus-visible:border-key has-focus-visible:shadow-[0_0_0_3px_var(--keyColor-focus-color)]',
  ].join(' '),
  swatch: 'relative flex h-14 items-end overflow-hidden rounded-[7px] p-1.5 shadow-[inset_0_0_0_0.5px_rgb(0_0_0/0.15)]',
  swatchSurface: 'flex h-7 w-3/4 items-center gap-1.5 rounded-[5px] px-1.5 shadow-[0_1px_2px_rgb(0_0_0/0.12)]',
  swatchAccent: 'size-3.5 flex-none rounded-full',
  swatchAccent2: 'h-1.5 flex-1 rounded-full',
  themeName: 'flex items-center justify-between gap-1 px-0.5 pb-0.5',
  themeCheck: 'flex-none text-key',
};
